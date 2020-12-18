package com.foxminded.university.dao.jdbc;

import static java.sql.Types.DATE;
import static java.sql.Types.INTEGER;
import static java.sql.Types.OTHER;
import static java.sql.Types.VARCHAR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@Component
public class JdbcStudentDao implements StudentDao {

	private static final String STUDENTS_TABLE_NAME = "students";
	private static final String STUDENT_ID = "student_id";
	private static final String GROUP_ID = "group_id";
	private static final String STUDENT_NAME = "student_name";
	private static final String STUDENT_SURNAME = "student_surname";
	private static final String STUDENT_PHONE = "student_phone";
	private static final String STUDENT_EMAIL = "student_email";
	private static final String STUDENT_ADDRESS = "student_address";
	private static final String STUDENT_BIRTHDATE = "student_birthdate";
	private static final String STUDENT_GENDER = "student_gender";
	private static final String FIND_STUDENT_BY_ID_QUERY = "SELECT * FROM students WHERE student_id = ?;";
	private static final String GET_STUDENTS_QUERY = "SELECT * FROM students;";
	private static final String DELETE_STUDENT_BY_ID_QUERY = "DELETE FROM students WHERE student_id = ?;";
	private static final String UPDATE_STUDENT_QUERY = "UPDATE students SET group_id = ?, student_name = ?, student_surname = ?, student_phone = ?, student_email = ?, student_address = ?, student_birthdate = ?, student_gender = ? "
			+ "WHERE student_id = ?;";
	private static final String GET_STUDENTS_BY_GROUP_ID_QUERY = "SELECT * FROM students WHERE group_id = ?;";
	private static final String CREATE_STUDENT_COURSE_QUERY = "INSERT INTO students_courses (student_id, course_id) VALUES(?, ?);";
	private static final String DELETE_STUDENT_COURSE_QUERY = "DELETE FROM students_courses WHERE student_id = ? AND course_id =?;";
	private static final String GET_STUDENTS_BY_COURSE_ID_QUERY = "SELECT * FROM students "
			+ "JOIN students_courses ON students_courses.student_id = students.student_id WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;
	private JdbcGroupDao jdbcGroupDao;

	@Autowired
	public JdbcStudentDao(DataSource dataSource, JdbcGroupDao jdbcGroupDao) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(STUDENTS_TABLE_NAME)
				.usingGeneratedKeyColumns(STUDENT_ID);
		this.jdbcGroupDao = jdbcGroupDao;
	}

	@Override
	public void create(Student student) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(GROUP_ID, Optional.ofNullable(student).map(Student::getGroup).map(Group::getId).orElse(null));
		parameters.put(STUDENT_NAME, student.getName());
		parameters.put(STUDENT_SURNAME, student.getSurname());
		parameters.put(STUDENT_PHONE, student.getPhone());
		parameters.put(STUDENT_EMAIL, student.getEmail());
		parameters.put(STUDENT_ADDRESS, student.getAddress());
		parameters.put(STUDENT_BIRTHDATE, student.getBirthdate());
		parameters.put(STUDENT_GENDER, student.getGender());
		student.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());
	}

	@Override
	public Student findById(Long studentId) {
		return jdbcTemplate
				.queryForObject(FIND_STUDENT_BY_ID_QUERY, new Object[] { studentId }, new StudentMapper(jdbcGroupDao));
	}

	@Override
	public List<Student> getAll() {
		return jdbcTemplate.query(GET_STUDENTS_QUERY, new StudentMapper(jdbcGroupDao));
	}

	@Override
	public void update(Student student) {
		jdbcTemplate.update(UPDATE_STUDENT_QUERY,
				new Object[] { Optional.ofNullable(student).map(Student::getGroup).map(Group::getId).orElse(null),
						student.getName(), student.getSurname(), student.getPhone(), student.getEmail(),
						student.getAddress(), student.getBirthdate(), student.getGender(), student.getId() },
				new int[] { INTEGER, VARCHAR, VARCHAR, VARCHAR, VARCHAR, VARCHAR, DATE, OTHER, INTEGER });
	}

	@Override
	public void deleteById(Long studentId) {
		jdbcTemplate.update(DELETE_STUDENT_BY_ID_QUERY, studentId);
	}

	@Override
	public List<Student> getStudentsByGroupId(Long groupId) {
		return jdbcTemplate
				.query(GET_STUDENTS_BY_GROUP_ID_QUERY, new Object[] { groupId }, new StudentMapper(jdbcGroupDao));
	}

	@Override
	public void createStudentCourse(Long studentId, Long courseId) {
		jdbcTemplate.update(CREATE_STUDENT_COURSE_QUERY, studentId, courseId);
	}

	@Override
	public void deleteStudentCourse(Long studentId, Long courseId) {
		jdbcTemplate.update(DELETE_STUDENT_COURSE_QUERY, studentId, courseId);
	}

	@Override
	public List<Student> getStudentsByCourseId(Long courseId) {
		return jdbcTemplate
				.query(GET_STUDENTS_BY_COURSE_ID_QUERY, new Object[] { courseId }, new StudentMapper(jdbcGroupDao));
	}
}
