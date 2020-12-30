package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.TeacherMapper.TEACHER_ID;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.mapper.TeacherMapper;
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
	private static final String CREATE_TEACHERS_COURSE_QUERY = "INSERT INTO teachers_courses (teacher_id, course_id) VALUES(?, ?);";
	private static final String DELETE_TEACHERS_COURSE_QUERY = "DELETE FROM teachers_courses WHERE teacher_id = ? AND course_id =?;";
	private static final String GET_TEACHERS_BY_COURSE_ID_QUERY = "SELECT * FROM teachers "
			+ "JOIN teachers_courses ON teachers_courses.teacher_id = teachers.id WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;

	public JdbcTeacherDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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
	}

	@Override
	public Teacher findById(Long teacherId) {
		return jdbcTemplate.queryForObject(FIND_TEACHER_BY_ID_QUERY, new Object[] { teacherId }, new TeacherMapper());
	}

	@Override
	public List<Teacher> getAll() {
		return jdbcTemplate.query(GET_TEACHERS_QUERY, new TeacherMapper());
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
	}

	@Override
	public void deleteById(Long teacherId) {
		jdbcTemplate.update(DELETE_TEACHER_BY_ID_QUERY, teacherId);
	}

	@Override
	public void createTeacherCourse(Long teacherId, Long courseId) {
		jdbcTemplate.update(CREATE_TEACHERS_COURSE_QUERY, teacherId, courseId);
	}

	@Override
	public void deleteTeacherCourse(Long teacherId, Long courseId) {
		jdbcTemplate.update(DELETE_TEACHERS_COURSE_QUERY, teacherId, courseId);
	}

	@Override
	public List<Teacher> getTeachersByCourseId(Long courseId) {
		return jdbcTemplate.query(GET_TEACHERS_BY_COURSE_ID_QUERY, new Object[] { courseId }, new TeacherMapper());
	}
}
