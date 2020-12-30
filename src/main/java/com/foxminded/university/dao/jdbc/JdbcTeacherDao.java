package com.foxminded.university.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.jdbc.mapper.TeacherMapper;
import com.foxminded.university.model.Teacher;

@Component
public class JdbcTeacherDao implements TeacherDao {

	private static final String TEACHERS_TABLE_NAME = "teachers";
	private static final String TEACHER_ID = "teacher_id";
	private static final String TEACHER_NAME = "teacher_name";
	private static final String TEACHER_SURNAME = "teacher_surname";
	private static final String TEACHER_RANK = "teacher_rank";
	private static final String TEACHER_PHONE = "teacher_phone";
	private static final String TEACHER_EMAIL = "teacher_phone";
	private static final String TEACHER_ADDRESS = "teacher_address";
	private static final String TEACHER_BIRTHDATE = "teacher_birthdate";
	private static final String TEACHER_GENDER = "teacher_gender";
	private static final String FIND_TEACHER_BY_ID_QUERY = "SELECT * FROM teachers WHERE teacher_id = ?;";
	private static final String GET_TEACHERS_QUERY = "SELECT * FROM teachers;";
	private static final String DELETE_TEACHER_BY_ID_QUERY = "DELETE FROM teachers WHERE teacher_id = ?;";
	private static final String UPDATE_TEACHER_QUERY = "UPDATE teachers SET teacher_name = ?, teacher_surname = ?, teacher_rank = ?, teacher_phone = ?, teacher_email = ?, teacher_address = ?, teacher_birthdate = ?, teacher_gender = ? "
			+ "WHERE teacher_id = ?;";
	private static final String CREATE_TEACHERS_COURSE_QUERY = "INSERT INTO teachers_courses (teacher_id, course_id) VALUES(?, ?);";
	private static final String DELETE_TEACHERS_COURSE_QUERY = "DELETE FROM teachers_courses WHERE teacher_id = ? AND course_id =?;";
	private static final String GET_TEACHERS_BY_COURSE_ID_QUERY = "SELECT * FROM teachers "
			+ "JOIN teachers_courses ON teachers_courses.teacher_id = teachers.teacher_id WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;

	public JdbcTeacherDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(TEACHERS_TABLE_NAME)
				.usingGeneratedKeyColumns(TEACHER_ID);
	}

	@Override
	public void create(Teacher teacher) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(TEACHER_NAME, teacher.getName());
		parameters.put(TEACHER_SURNAME, teacher.getSurname());
		parameters.put(TEACHER_RANK, teacher.getRank());
		parameters.put(TEACHER_PHONE, teacher.getPhone());
		parameters.put(TEACHER_EMAIL, teacher.getEmail());
		parameters.put(TEACHER_ADDRESS, teacher.getAddress());
		parameters.put(TEACHER_BIRTHDATE, teacher.getBirthdate());
		parameters.put(TEACHER_GENDER, teacher.getGender());
		teacher.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());
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
				teacher.getBirthdate(),
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
