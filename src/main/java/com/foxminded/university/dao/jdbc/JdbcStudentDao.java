package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_ADDRESS;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_BIRTHDATE;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_EMAIL;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_GENDER;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_ID;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_NAME;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_PHONE;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_SURNAME;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@Component
public class JdbcStudentDao implements StudentDao {

	private static final String CREATE_STUDENT_QUERY = "INSERT INTO students (group_id, name, surname, phone, email, address, birthdate, gender) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String FIND_STUDENT_BY_ID_QUERY = "SELECT * FROM students WHERE id = ?;";
	private static final String GET_STUDENTS_QUERY = "SELECT * FROM students;";
	private static final String DELETE_STUDENT_BY_ID_QUERY = "DELETE FROM students WHERE id = ?;";
	private static final String UPDATE_STUDENT_QUERY = "UPDATE students SET id = ?, name = ?, surname = ?, phone = ?, email = ?, address = ?, birthdate = ?, gender = ? "
			+ "WHERE id = ?;";
	private static final String GET_STUDENTS_BY_GROUP_ID_QUERY = "SELECT * FROM students WHERE group_id = ?;";
	private static final String CREATE_STUDENT_COURSE_QUERY = "INSERT INTO students_courses (student_id, course_id) VALUES(?, ?);";
	private static final String DELETE_STUDENT_COURSE_QUERY = "DELETE FROM students_courses WHERE student_id = ? AND course_id =?;";
	private static final String GET_STUDENTS_BY_COURSE_ID_QUERY = "SELECT * FROM students "
			+ "JOIN students_courses ON students_courses.student_id = students.id WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private JdbcGroupDao groupDao;

	public JdbcStudentDao(JdbcTemplate jdbcTemplate, JdbcGroupDao groupDao) {
		this.jdbcTemplate = jdbcTemplate;
		this.groupDao = groupDao;
	}

	@Override
	public void create(Student student) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(CREATE_STUDENT_QUERY,
					new String[] { STUDENT_ID });
			statement.setObject(1, Optional.ofNullable(student).map(Student::getGroup).map(Group::getId).orElse(null));
			statement.setString(2, student.getName());
			statement.setString(3, student.getSurname());
			statement.setString(4, student.getPhone());
			statement.setString(5, student.getEmail());
			statement.setString(6, student.getAddress());
			statement.setObject(7, student.getBirthdate());
			statement.setString(8, student.getGender().toString());
			return statement;
		}, keyHolder);
		student.setId(keyHolder.getKey().longValue());
	}

	@Override
	public Student findById(Long studentId) {
		return jdbcTemplate
				.queryForObject(FIND_STUDENT_BY_ID_QUERY, new Object[] { studentId }, new StudentMapper(groupDao));
	}

	@Override
	public List<Student> getAll() {
		return jdbcTemplate.query(GET_STUDENTS_QUERY, new StudentMapper(groupDao));
	}

	@Override
	public void update(Student student) {
		jdbcTemplate.update(UPDATE_STUDENT_QUERY,
				Optional.ofNullable(student).map(Student::getGroup).map(Group::getId).orElse(null),
				student.getName(),
				student.getSurname(),
				student.getPhone(),
				student.getEmail(),
				student.getAddress(),
				student.getBirthdate(),
				student.getGender().toString(),
				student.getId());
	}

	@Override
	public void deleteById(Long studentId) {
		jdbcTemplate.update(DELETE_STUDENT_BY_ID_QUERY, studentId);
	}

	@Override
	public List<Student> getStudentsByGroup(Group group) {
		return jdbcTemplate.query(GET_STUDENTS_BY_GROUP_ID_QUERY, new Object[] { group.getId() }, (rs, rowNum) -> {
			Student student = new Student(rs.getString(STUDENT_NAME), rs.getString(STUDENT_SURNAME));
			student.setId(rs.getLong(STUDENT_ID));
			student.setPhone(rs.getString(STUDENT_PHONE));
			student.setEmail(rs.getString(STUDENT_EMAIL));
			student.setAddress(rs.getString(STUDENT_ADDRESS));
			student.setBirthdate(rs.getObject(STUDENT_BIRTHDATE, LocalDate.class));
			student.setGender(Gender.valueOf(rs.getString(STUDENT_GENDER)));
			student.setGroup(group);
			return student;
		});
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
				.query(GET_STUDENTS_BY_COURSE_ID_QUERY, new Object[] { courseId }, new StudentMapper(groupDao));
	}
}
