package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.CourseMapper.COURSE_ID;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;

@Component
public class JdbcCourseDao implements CourseDao {

	private static final String CREATE_COURSE_QUERY = "INSERT INTO courses (name, description) VALUES (?, ?);";
	private static final String FIND_COURSE_BY_ID_QUERY = "SELECT * FROM courses WHERE id = ?";
	private static final String GET_COURSES_QUERY = "SELECT * FROM courses";
	private static final String DELETE_COURSE_BY_ID_QUERY = "DELETE FROM courses WHERE id = ?;";
	private static final String UPDATE_COURSE_QUERY = "UPDATE courses SET name = ?, description = ? WHERE id = ?;";
	private static final String CREATE_COURSE_ROOM_QUERY = "INSERT INTO courses_rooms (course_id, room_id) VALUES (?, ?);";
	private static final String DELETE_COURSE_ROOM_QUERY = "DELETE FROM courses_rooms WHERE course_id = ? AND room_id = ?;";
	private static final String GET_COURSES_BY_ROOM_ID_QUERY = "SELECT * FROM courses JOIN courses_rooms ON courses_rooms.course_id = courses.id WHERE room_id = ?;";
	private static final String GET_COURSES_BY_STUDENT_ID_QUERY = "SELECT * FROM courses JOIN students_courses ON students_courses.course_id = courses.id WHERE student_id = ?;";
	private static final String GET_COURSES_BY_TEACHER_ID_QUERY = "SELECT * FROM courses JOIN teachers_courses ON teachers_courses.course_id = courses.id WHERE teacher_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private JdbcRoomDao roomDao;
	private CourseMapper courseMapper;

	public JdbcCourseDao(JdbcTemplate jdbcTemplate, JdbcRoomDao roomDao, CourseMapper courseMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.roomDao = roomDao;
		this.courseMapper = courseMapper;
	}

	@Override
	public void create(Course course) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(CREATE_COURSE_QUERY, new String[] { COURSE_ID });
			statement.setString(1, course.getName());
			statement.setString(2, course.getDescription());
			return statement;
		}, keyHolder);
		course.setId(keyHolder.getKey().longValue());
		course.getRooms()
				.stream()
				.forEach(r -> jdbcTemplate.update(CREATE_COURSE_ROOM_QUERY, course.getId(), r.getId()));

	}

	@Override
	public Course findById(Long courseId) {
		return jdbcTemplate.queryForObject(FIND_COURSE_BY_ID_QUERY, new Object[] { courseId }, courseMapper);
	}

	@Override
	public List<Course> getAll() {
		return jdbcTemplate.query(GET_COURSES_QUERY, courseMapper);
	}

	@Override
	public void update(Course course) {
		jdbcTemplate.update(UPDATE_COURSE_QUERY, course.getName(), course.getDescription(), course.getId());
		List<Room> rooms = roomDao.getRoomsByCourseId(course.getId());
		rooms.stream()
				.filter(r -> !course.getRooms().contains(r))
				.forEach(r -> jdbcTemplate.update(DELETE_COURSE_ROOM_QUERY, course.getId(), r.getId()));
		course.getRooms()
				.stream()
				.filter(r -> !rooms.contains(r))
				.forEach(r -> jdbcTemplate.update(CREATE_COURSE_ROOM_QUERY, course.getId(), r.getId()));
	}

	@Override
	public void deleteById(Long courseId) {
		jdbcTemplate.update(DELETE_COURSE_BY_ID_QUERY, courseId);
	}

	@Override
	public List<Course> getCoursesByRoomId(Long roomId) {
		return jdbcTemplate.query(GET_COURSES_BY_ROOM_ID_QUERY, new Object[] { roomId }, courseMapper);
	}

	@Override
	public List<Course> getCoursesByStudentId(Long studentId) {
		return jdbcTemplate.query(GET_COURSES_BY_STUDENT_ID_QUERY, new Object[] { studentId }, courseMapper);
	}

	@Override
	public List<Course> getCoursesByTeacherId(Long teacherId) {
		return jdbcTemplate.query(GET_COURSES_BY_TEACHER_ID_QUERY, new Object[] { teacherId }, courseMapper);
	}
}
