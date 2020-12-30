package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.CourseMapper.COURSE_ID;
import static com.foxminded.university.dao.jdbc.mapper.LessonMapper.LESSON_DATE;
import static com.foxminded.university.dao.jdbc.mapper.LessonMapper.LESSON_ID;
import static com.foxminded.university.dao.jdbc.mapper.RoomMapper.ROOM_ID;
import static com.foxminded.university.dao.jdbc.mapper.TeacherMapper.TEACHER_ID;
import static com.foxminded.university.dao.jdbc.mapper.TimeframeMapper.TIMEFRAME_ID;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.jdbc.mapper.LessonMapper;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

@Component
public class JdbcLessonDao implements LessonDao {

	private static final String LESSONS_TABLE_NAME = "lessons";
	private static final String FIND_LESSON_BY_ID_QUERY = "SELECT * FROM lessons WHERE lesson_id = ?;";
	private static final String GET_LESSONS_QUERY = "SELECT * FROM lessons";
	private static final String DELETE_LESSON_BY_ID_QUERY = "DELETE FROM lessons WHERE lesson_id = ?";
	private static final String UPDATE_LESSON_QUERY = "UPDATE lessons SET lesson_date = ?, timeframe_id = ?, course_id = ?, teacher_id = ?, room_id = ? "
			+ "WHERE lesson_id = ?";
	private static final String CREATE_LESSON_GROUP_QUERY = "INSERT INTO lessons_groups (lesson_id, group_id) VALUES(?, ?);";
	private static final String DELETE_LESSON_GROUP_QUERY = "DELETE FROM lessons_groups WHERE lesson_id = ? AND group_id =?;";
	private static final String GET_LESSONS_BY_GROUP_ID_QUERY = "SELECT * FROM lessons "
			+ "JOIN lessons_groups ON lessons_groups.lesson_id = lessons.lesson_id WHERE group_id = ?;";
	private static final String GET_LESSONS_BY_TIMEFRAME_ID_QUERY = "SELECT * FROM lessons WHERE timeframe_id = ?";
	private static final String GET_LESSONS_BY_COURSE_ID_QUERY = "SELECT * FROM lessons WHERE course_id = ?";
	private static final String GET_LESSONS_BY_TEACHER_ID_QUERY = "SELECT * FROM lessons WHERE teacher_id = ?";
	private static final String GET_LESSONS_BY_ROOM_ID_QUERY = "SELECT * FROM lessons WHERE room_id = ?";

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	private JdbcTimeframeDao timeframeDao;
	private JdbcCourseDao courseDao;
	private JdbcTeacherDao teacherDao;
	private JdbcRoomDao roomDao;

	public JdbcLessonDao(JdbcTemplate jdbcTemplate, JdbcTimeframeDao timeframeDao, JdbcCourseDao courseDao,
			JdbcTeacherDao teacherDao, JdbcRoomDao roomDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(LESSONS_TABLE_NAME)
				.usingGeneratedKeyColumns(LESSON_ID);
		this.timeframeDao = timeframeDao;
		this.courseDao = courseDao;
		this.teacherDao = teacherDao;
		this.roomDao = roomDao;
	}

	@Override
	public void create(Lesson lesson) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(LESSON_DATE, lesson.getDate());
		parameters.put(TIMEFRAME_ID, lesson.getTimeframe().getId());
		parameters.put(COURSE_ID, lesson.getCourse().getId());
		parameters.put(TEACHER_ID, lesson.getTeacher().getId());
		parameters.put(ROOM_ID, lesson.getRoom().getId());
		lesson.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());
	}

	@Override
	public Lesson findById(Long lessonId) {
		return jdbcTemplate.queryForObject(FIND_LESSON_BY_ID_QUERY,
				new Object[] { lessonId },
				new LessonMapper(timeframeDao, courseDao, teacherDao, roomDao));
	}

	@Override
	public List<Lesson> getAll() {
		return jdbcTemplate.query(GET_LESSONS_QUERY, new LessonMapper(timeframeDao, courseDao, teacherDao, roomDao));
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
	}

	@Override
	public void deleteById(Long lessonId) {
		jdbcTemplate.update(DELETE_LESSON_BY_ID_QUERY, lessonId);
	}

	@Override
	public void createLessonsGroups(Long lessonId, Long groupId) {
		jdbcTemplate.update(CREATE_LESSON_GROUP_QUERY, lessonId, groupId);

	}

	@Override
	public void deleteLessonsGroups(Long lessonId, Long groupId) {
		jdbcTemplate.update(DELETE_LESSON_GROUP_QUERY, lessonId, groupId);
	}

	@Override
	public List<Lesson> getLessonsByGroupId(Long groupId) {
		return jdbcTemplate.query(GET_LESSONS_BY_GROUP_ID_QUERY,
				new Object[] { groupId },
				new LessonMapper(timeframeDao, courseDao, teacherDao, roomDao));
	}

	@Override
	public List<Lesson> getLessonsByTimeframe(Timeframe timeframe) {
		return jdbcTemplate
				.query(GET_LESSONS_BY_TIMEFRAME_ID_QUERY, new Object[] { timeframe.getId() }, (rs, rowNum) -> {
					Lesson lesson = new Lesson();
					lesson.setId(rs.getLong(LESSON_ID));
					lesson.setDate(rs.getObject(LESSON_DATE, LocalDate.class));
					lesson.setTimeframe(timeframe);
					lesson.setCourse(courseDao.findById(rs.getLong(COURSE_ID)));
					lesson.setTeacher(teacherDao.findById(rs.getLong(TEACHER_ID)));
					lesson.setRoom(roomDao.findById(rs.getLong(ROOM_ID)));
					return lesson;
				});
	}

	@Override
	public List<Lesson> getLessonsByCourse(Course course) {
		return jdbcTemplate.query(GET_LESSONS_BY_COURSE_ID_QUERY, new Object[] { course.getId() }, (rs, rowNum) -> {
			Lesson lesson = new Lesson();
			lesson.setId(rs.getLong(LESSON_ID));
			lesson.setDate(rs.getObject(LESSON_DATE, LocalDate.class));
			lesson.setTimeframe(timeframeDao.findById(rs.getLong(TIMEFRAME_ID)));
			lesson.setCourse(course);
			lesson.setTeacher(teacherDao.findById(rs.getLong(TEACHER_ID)));
			lesson.setRoom(roomDao.findById(rs.getLong(ROOM_ID)));
			return lesson;
		});
	}

	@Override
	public List<Lesson> getLessonsByTeacher(Teacher teacher) {
		return jdbcTemplate.query(GET_LESSONS_BY_TEACHER_ID_QUERY, new Object[] { teacher.getId() }, (rs, rowNum) -> {
			Lesson lesson = new Lesson();
			lesson.setId(rs.getLong(LESSON_ID));
			lesson.setDate(rs.getObject(LESSON_DATE, LocalDate.class));
			lesson.setTimeframe(timeframeDao.findById(rs.getLong(TIMEFRAME_ID)));
			lesson.setCourse(courseDao.findById(rs.getLong(COURSE_ID)));
			lesson.setTeacher(teacher);
			lesson.setRoom(roomDao.findById(rs.getLong(ROOM_ID)));
			return lesson;
		});
	}

	@Override
	public List<Lesson> getLessonsByRoom(Room room) {
		return jdbcTemplate.query(GET_LESSONS_BY_ROOM_ID_QUERY, new Object[] { room.getId() }, (rs, rowNum) -> {
			Lesson lesson = new Lesson();
			lesson.setId(rs.getLong(LESSON_ID));
			lesson.setDate(rs.getObject(LESSON_DATE, LocalDate.class));
			lesson.setTimeframe(timeframeDao.findById(rs.getLong(TIMEFRAME_ID)));
			lesson.setCourse(courseDao.findById(rs.getLong(COURSE_ID)));
			lesson.setTeacher(teacherDao.findById(rs.getLong(TEACHER_ID)));
			lesson.setRoom(room);
			return lesson;
		});
	}
}
