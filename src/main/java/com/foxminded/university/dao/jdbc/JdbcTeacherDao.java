package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.TeacherMapper.TEACHER_ID;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.mapper.TeacherMapper;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

@Component
public class JdbcTeacherDao implements TeacherDao {

	private static final String CREATE_TEACHER_QUERY = "INSERT INTO teachers (name, surname, rank, phone, email, address, birth_date, gender) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String FIND_TEACHER_BY_ID_QUERY = "SELECT * FROM teachers WHERE id = ?;";
	private static final String GET_TEACHERS_QUERY = "SELECT * FROM teachers;";
	private static final String DELETE_TEACHER_BY_ID_QUERY = "DELETE FROM teachers WHERE id = ?;";
	private static final String UPDATE_TEACHER_QUERY = "UPDATE teachers SET name = ?, surname = ?, rank = ?, phone = ?, email = ?, address = ?, birth_date = ?, gender = ? "
			+ "WHERE id = ?;";
	private static final String CREATE_TEACHER_COURSE_QUERY = "INSERT INTO teachers_courses (teacher_id, course_id) VALUES(?, ?);";
	private static final String DELETE_TEACHER_COURSE_QUERY = "DELETE FROM teachers_courses WHERE teacher_id = ? AND course_id =?;";
	private static final String GET_TEACHERS_BY_COURSE_ID_QUERY = "SELECT * FROM teachers "
			+ "JOIN teachers_courses ON teachers_courses.teacher_id = teachers.id WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private JdbcCourseDao courseDao;
	private TeacherMapper teacherMapper;

	public JdbcTeacherDao(JdbcTemplate jdbcTemplate, JdbcCourseDao courseDao, TeacherMapper teacherMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.courseDao = courseDao;
		this.teacherMapper = teacherMapper;
	}

	@Override
	public void create(Teacher teacher) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(CREATE_TEACHER_QUERY,
					new String[] { TEACHER_ID });
			statement.setString(1, teacher.getName());
			statement.setString(2, teacher.getSurname());
			statement.setString(3, teacher.getRank());
			statement.setString(4, teacher.getPhone());
			statement.setString(5, teacher.getEmail());
			statement.setString(6, teacher.getAddress());
			statement.setObject(7, teacher.getBirthDate());
			statement.setString(8, teacher.getGender().toString());
			return statement;
		}, keyHolder);
		teacher.setId(keyHolder.getKey().longValue());
		teacher.getCourses()
				.stream()
				.forEach(c -> jdbcTemplate.update(CREATE_TEACHER_COURSE_QUERY, teacher.getId(), c.getId()));
	}

	@Override
	public Optional<Teacher> findById(Long teacherId) {
		try {
			return Optional.of(
					jdbcTemplate.queryForObject(FIND_TEACHER_BY_ID_QUERY, new Object[] { teacherId }, teacherMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Teacher> getAll() {
		return jdbcTemplate.query(GET_TEACHERS_QUERY, teacherMapper);
	}

	@Override
	public void update(Teacher teacher) {
		jdbcTemplate.update(UPDATE_TEACHER_QUERY,
				teacher.getName(),
				teacher.getSurname(),
				teacher.getRank(),
				teacher.getPhone(),
				teacher.getEmail(),
				teacher.getAddress(),
				teacher.getBirthDate(),
				teacher.getGender().toString(),
				teacher.getId());
		List<Course> courses = courseDao.getCoursesByTeacherId(teacher.getId());
		courses.stream()
				.filter(c -> !teacher.getCourses().contains(c))
				.forEach(c -> jdbcTemplate.update(DELETE_TEACHER_COURSE_QUERY, teacher.getId(), c.getId()));
		teacher.getCourses()
				.stream()
				.filter(c -> !courses.contains(c))
				.forEach(c -> jdbcTemplate.update(CREATE_TEACHER_COURSE_QUERY, teacher.getId(), c.getId()));
	}

	@Override
	public void deleteById(Long teacherId) {
		jdbcTemplate.update(DELETE_TEACHER_BY_ID_QUERY, teacherId);
	}

	@Override
	public List<Teacher> getTeachersByCourseId(Long courseId) {
		return jdbcTemplate.query(GET_TEACHERS_BY_COURSE_ID_QUERY, new Object[] { courseId }, teacherMapper);
	}
}
