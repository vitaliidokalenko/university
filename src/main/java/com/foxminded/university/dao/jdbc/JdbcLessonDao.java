package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.LessonMapper.LESSON_ID;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.LessonDao;
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
	private static final String GET_LESSONS_BY_GROUP_ID_AND_DATE_QUERY = "SELECT * FROM lessons "
			+ "JOIN lessons_groups ON lessons_groups.lesson_id = lessons.id WHERE group_id = ? AND date = ?";
	private static final String GET_LESSONS_BY_TIMEFRAME_ID_QUERY = "SELECT * FROM lessons WHERE timeframe_id = ?";
	private static final String GET_LESSONS_BY_COURSE_ID_QUERY = "SELECT * FROM lessons WHERE course_id = ?";
	private static final String GET_LESSONS_BY_TEACHER_ID_AND_DATE_QUERY = "SELECT * FROM lessons WHERE teacher_id = ? AND date = ?";
	private static final String GET_LESSONS_BY_ROOM_ID_AND_DATE_QUERY = "SELECT * FROM lessons WHERE room_id = ? AND date = ?";

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
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(CREATE_LESSON_QUERY, new String[] { LESSON_ID });
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
	}

	@Override
	public Optional<Lesson> findById(Long lessonId) {
		try {
			return Optional
					.of(jdbcTemplate.queryForObject(FIND_LESSON_BY_ID_QUERY, new Object[] { lessonId }, lessonMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Lesson> getAll() {
		return jdbcTemplate.query(GET_LESSONS_QUERY, lessonMapper);
	}

	@Override
	public void update(Lesson lesson) {
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
	}

	@Override
	public void deleteById(Long lessonId) {
		jdbcTemplate.update(DELETE_LESSON_BY_ID_QUERY, lessonId);
	}

	@Override
	public List<Lesson> getByGroupIdAndDate(Long groupId, LocalDate date) {
		return jdbcTemplate.query(GET_LESSONS_BY_GROUP_ID_AND_DATE_QUERY, new Object[] { groupId, date }, lessonMapper);
	}

	@Override
	public List<Lesson> getByTimeframe(Timeframe timeframe) {
		return jdbcTemplate
				.query(GET_LESSONS_BY_TIMEFRAME_ID_QUERY, new Object[] { timeframe.getId() }, lessonMapper);
	}

	@Override
	public List<Lesson> getByCourse(Course course) {
		return jdbcTemplate.query(GET_LESSONS_BY_COURSE_ID_QUERY, new Object[] { course.getId() }, lessonMapper);
	}

	@Override
	public List<Lesson> getByTeacherAndDate(Teacher teacher, LocalDate date) {
		return jdbcTemplate
				.query(GET_LESSONS_BY_TEACHER_ID_AND_DATE_QUERY, new Object[] { teacher.getId(), date }, lessonMapper);
	}

	@Override
	public List<Lesson> getByRoomAndDate(Room room, LocalDate date) {
		return jdbcTemplate
				.query(GET_LESSONS_BY_ROOM_ID_AND_DATE_QUERY, new Object[] { room.getId(), date }, lessonMapper);
	}
}
