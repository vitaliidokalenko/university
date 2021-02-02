package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.LessonMapper.LESSON_ID;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.exception.DAOException;
import com.foxminded.university.dao.jdbc.mapper.LessonMapper;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

@Component
public class JdbcLessonDao implements LessonDao {

	private static final String CREATE_LESSON_QUERY = "INSERT INTO lessons (date, timeframe_id, course_id, teacher_id, room_id) "
			+ "VALUES (?, ?, ?, ?, ?)";
	private static final String FIND_LESSON_BY_ID_QUERY = "SELECT * FROM lessons WHERE id = ?";
	private static final String GET_LESSONS_QUERY = "SELECT * FROM lessons";
	private static final String DELETE_LESSON_BY_ID_QUERY = "DELETE FROM lessons WHERE id = ?";
	private static final String UPDATE_LESSON_QUERY = "UPDATE lessons SET date = ?, timeframe_id = ?, course_id = ?, teacher_id = ?, room_id = ? "
			+ "WHERE id = ?";
	private static final String CREATE_LESSON_GROUP_QUERY = "INSERT INTO lessons_groups (lesson_id, group_id) VALUES(?, ?)";
	private static final String DELETE_LESSON_GROUP_QUERY = "DELETE FROM lessons_groups WHERE lesson_id = ? AND group_id = ?";
	private static final String GET_LESSON_BY_GROUP_ID_AND_DATE_AND_TIMEFRAME_ID_QUERY = "SELECT * FROM lessons "
			+ "JOIN lessons_groups ON lessons_groups.lesson_id = lessons.id WHERE group_id = ? AND date = ? AND timeframe_id = ?";
	private static final String GET_LESSONS_BY_TIMEFRAME_ID_QUERY = "SELECT * FROM lessons WHERE timeframe_id = ?";
	private static final String GET_LESSONS_BY_COURSE_ID_QUERY = "SELECT * FROM lessons WHERE course_id = ?";
	private static final String GET_LESSON_BY_TEACHER_ID_AND_DATE_AND_TIMEFRAME_ID_QUERY = "SELECT * FROM lessons WHERE teacher_id = ? AND date = ? AND timeframe_id = ?";
	private static final String GET_LESSON_BY_ROOM_ID_AND_DATE_AND_TIMEFRAME_ID_QUERY = "SELECT * FROM lessons WHERE room_id = ? AND date = ? AND timeframe_id = ?";

	private JdbcTemplate jdbcTemplate;
	private JdbcGroupDao groupDao;
	private LessonMapper lessonMapper;

	public JdbcLessonDao(JdbcTemplate jdbcTemplate, JdbcGroupDao groupDao, LessonMapper lessonMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.groupDao = groupDao;
		this.lessonMapper = lessonMapper;
	}

	@Override
	public void create(Lesson lesson) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(CREATE_LESSON_QUERY,
						new String[] { LESSON_ID });
				statement.setObject(1, lesson.getDate());
				statement.setLong(2, lesson.getTimeframe().getId());
				statement.setLong(3, lesson.getCourse().getId());
				statement.setLong(4, lesson.getTeacher().getId());
				statement.setLong(5, lesson.getRoom().getId());
				return statement;
			}, keyHolder);
			lesson.setId(keyHolder.getKey().longValue());
			lesson.getGroups()
					.stream()
					.forEach(g -> jdbcTemplate.update(CREATE_LESSON_GROUP_QUERY, lesson.getId(), g.getId()));
		} catch (DataAccessException e) {
			throw new DAOException("Could not create lesson: " + lesson, e);
		}
	}

	@Override
	public Optional<Lesson> findById(Long lessonId) {
		try {
			return Optional
					.of(jdbcTemplate.queryForObject(FIND_LESSON_BY_ID_QUERY, new Object[] { lessonId }, lessonMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DAOException("Could not get lesson by id: " + lessonId, e);
		}
	}

	@Override
	public List<Lesson> getAll() {
		try {
			return jdbcTemplate.query(GET_LESSONS_QUERY, lessonMapper);
		} catch (DataAccessException e) {
			throw new DAOException("Could not get lessons", e);
		}
	}

	@Override
	public void update(Lesson lesson) {
		try {
			jdbcTemplate.update(UPDATE_LESSON_QUERY,
					lesson.getDate(),
					lesson.getTimeframe().getId(),
					lesson.getCourse().getId(),
					lesson.getTeacher().getId(),
					lesson.getRoom().getId(),
					lesson.getId());
			List<Group> groups = groupDao.getByLessonId(lesson.getId());
			groups.stream()
					.filter(g -> !lesson.getGroups().contains(g))
					.forEach(g -> jdbcTemplate.update(DELETE_LESSON_GROUP_QUERY, lesson.getId(), g.getId()));
			lesson.getGroups()
					.stream()
					.filter(g -> !groups.contains(g))
					.forEach(g -> jdbcTemplate.update(CREATE_LESSON_GROUP_QUERY, lesson.getId(), g.getId()));
		} catch (DataAccessException e) {
			throw new DAOException("Could not update lesson: " + lesson, e);
		}
	}

	@Override
	public void deleteById(Long lessonId) {
		try {
			jdbcTemplate.update(DELETE_LESSON_BY_ID_QUERY, lessonId);
		} catch (DataAccessException e) {
			throw new DAOException("Could not delete lesson by id: " + lessonId, e);
		}
	}

	@Override
	public Optional<Lesson> getByGroupIdAndDateAndTimeframe(Long groupId, LocalDate date, Timeframe timeframe) {
		try {
			return Optional.of(jdbcTemplate.queryForObject(GET_LESSON_BY_GROUP_ID_AND_DATE_AND_TIMEFRAME_ID_QUERY,
					new Object[] { groupId, date, timeframe.getId() },
					lessonMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DAOException(
					"Could not get lesson by group id: " + groupId + ", date: " + date + ", timeframe: " + timeframe,
					e);
		}
	}

	@Override
	public List<Lesson> getByTimeframe(Timeframe timeframe) {
		try {
			return jdbcTemplate
					.query(GET_LESSONS_BY_TIMEFRAME_ID_QUERY, new Object[] { timeframe.getId() }, lessonMapper);
		} catch (DataAccessException e) {
			throw new DAOException("Could not get lessons by timeframe: " + timeframe, e);
		}
	}

	@Override
	public List<Lesson> getByCourse(Course course) {
		try {
			return jdbcTemplate.query(GET_LESSONS_BY_COURSE_ID_QUERY, new Object[] { course.getId() }, lessonMapper);
		} catch (DataAccessException e) {
			throw new DAOException("Could not get lessons by course: " + course, e);
		}
	}

	@Override
	public Optional<Lesson> getByTeacherAndDateAndTimeframe(Teacher teacher, LocalDate date, Timeframe timeframe) {
		try {
			return Optional.of(jdbcTemplate
					.queryForObject(GET_LESSON_BY_TEACHER_ID_AND_DATE_AND_TIMEFRAME_ID_QUERY,
							new Object[] { teacher.getId(), date, timeframe.getId() },
							lessonMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DAOException(
					"Could not get lesson by teacher: " + teacher + ", date: " + date + ", timeframe: " + timeframe, e);
		}
	}

	@Override
	public Optional<Lesson> getByRoomAndDateAndTimeframe(Room room, LocalDate date, Timeframe timeframe) {
		try {
			return Optional.of(jdbcTemplate
					.queryForObject(GET_LESSON_BY_ROOM_ID_AND_DATE_AND_TIMEFRAME_ID_QUERY,
							new Object[] { room.getId(), date, timeframe.getId() },
							lessonMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DAOException(
					"Could not get lesson by room: " + room + ", date: " + date + ", timeframe: " + timeframe, e);
		}
	}
}
