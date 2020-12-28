package com.foxminded.university.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.jdbc.mapper.CourseMapper;
import com.foxminded.university.model.Course;

@Component
public class JdbcCourseDao implements CourseDao {

	private static final String COURSES_TABLE_NAME = "courses";
	private static final String COURSE_ID = "course_id";
	private static final String COURSE_NAME = "course_name";
	private static final String COURSE_DESCRIPTION = "course_description";
	private static final String FIND_COURSE_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id = ?";
	private static final String GET_COURSES_QUERY = "SELECT * FROM courses";
	private static final String DELETE_COURSE_BY_ID_QUERY = "DELETE FROM courses WHERE course_id = ?;";
	private static final String UPDATE_COURSE_QUERY = "UPDATE courses SET course_name = ?, course_description = ? WHERE course_id = ?;";
	private static final String CREATE_COURSE_ROOM_QUERY = "INSERT INTO courses_rooms (course_id, room_id) VALUES (?, ?);";
	private static final String DELETE_COURSE_ROOM_QUERY = "DELETE FROM courses_rooms WHERE course_id = ? AND room_id = ?;";
	private static final String GET_COURSES_BY_ROOM_ID_QUERY = "SELECT * FROM courses JOIN courses_rooms ON courses_rooms.course_id = courses.course_id WHERE room_id = ?;";
	private static final String GET_COURSES_BY_STUDENT_ID_QUERY = "SELECT * FROM courses JOIN students_courses ON students_courses.course_id = courses.course_id WHERE student_id = ?;";
	private static final String GET_COURSES_BY_TEACHER_ID_QUERY = "SELECT * FROM courses JOIN teachers_courses ON teachers_courses.course_id = courses.course_id WHERE teacher_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;

	@Autowired
	public JdbcCourseDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(COURSES_TABLE_NAME)
				.usingGeneratedKeyColumns(COURSE_ID);
	}

	@Override
	public void create(Course course) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(COURSE_NAME, course.getName());
		parameters.put(COURSE_DESCRIPTION, course.getDescription());
		course.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());
	}

	@Override
	public Course findById(Long courseId) {
		return jdbcTemplate.queryForObject(FIND_COURSE_BY_ID_QUERY, new Object[] { courseId }, new CourseMapper());
	}

	@Override
	public List<Course> getAll() {
		return jdbcTemplate.query(GET_COURSES_QUERY, new CourseMapper());
	}

	@Override
	public void update(Course course) {
		jdbcTemplate.update(UPDATE_COURSE_QUERY, course.getName(), course.getDescription(), course.getId());
	}

	@Override
	public void deleteById(Long courseId) {
		jdbcTemplate.update(DELETE_COURSE_BY_ID_QUERY, courseId);
	}

	@Override
	public void createCourseRoom(Long courseId, Long roomId) {
		jdbcTemplate.update(CREATE_COURSE_ROOM_QUERY, courseId, roomId);
	}

	@Override
	public void deleteCourseRoom(Long courseId, Long roomId) {
		jdbcTemplate.update(DELETE_COURSE_ROOM_QUERY, courseId, roomId);
	}

	@Override
	public List<Course> getCoursesByRoomId(Long roomId) {
		return jdbcTemplate.query(GET_COURSES_BY_ROOM_ID_QUERY, new Object[] { roomId }, new CourseMapper());
	}

	@Override
	public List<Course> getCoursesByStudentId(Long studentId) {
		return jdbcTemplate.query(GET_COURSES_BY_STUDENT_ID_QUERY, new Object[] { studentId }, new CourseMapper());
	}

	@Override
	public List<Course> getCoursesByTeacherId(Long teacherId) {
		return jdbcTemplate.query(GET_COURSES_BY_TEACHER_ID_QUERY, new Object[] { teacherId }, new CourseMapper());
	}
}
