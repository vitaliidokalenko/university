package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_ADDRESS;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_BIRTH_DATE;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_EMAIL;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_GENDER;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_ID;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_NAME;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_PHONE;
import static com.foxminded.university.dao.jdbc.mapper.StudentMapper.STUDENT_SURNAME;
import static java.util.stream.Collectors.toSet;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.exception.DaoException;
import com.foxminded.university.dao.jdbc.mapper.StudentMapper;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@Component
public class JdbcStudentDao implements StudentDao {

	private static final String CREATE_STUDENT_QUERY = "INSERT INTO students (group_id, name, surname, phone, email, address, birth_date, gender) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String FIND_STUDENT_BY_ID_QUERY = "SELECT * FROM students WHERE id = ?";
	private static final String GET_STUDENTS_QUERY = "SELECT * FROM students";
	private static final String DELETE_STUDENT_BY_ID_QUERY = "DELETE FROM students WHERE id = ?";
	private static final String UPDATE_STUDENT_QUERY = "UPDATE students SET group_id = ?, name = ?, surname = ?, phone = ?, email = ?, address = ?, birth_date = ?, gender = ? "
			+ "WHERE id = ?";
	private static final String GET_STUDENTS_BY_GROUP_ID_QUERY = "SELECT * FROM students WHERE group_id = ?";
	private static final String CREATE_STUDENT_COURSE_QUERY = "INSERT INTO students_courses (student_id, course_id) VALUES(?, ?)";
	private static final String DELETE_STUDENT_COURSE_QUERY = "DELETE FROM students_courses WHERE student_id = ? AND course_id =?";
	private static final String GET_STUDENTS_BY_COURSE_ID_QUERY = "SELECT * FROM students "
			+ "JOIN students_courses ON students_courses.student_id = students.id WHERE course_id = ?";
	private static final String COUNT_STUDENTS_QUERY = "SELECT count(*) FROM students";
	private static final String GET_STUDENTS_ORDERED_BY_NAME_WITH_LIMIT_AND_OFFSET_QUERY = "SELECT * FROM students ORDER BY name, surname LIMIT ? OFFSET ?";

	private JdbcTemplate jdbcTemplate;
	private JdbcCourseDao courseDao;
	private StudentMapper studentMapper;

	public JdbcStudentDao(JdbcTemplate jdbcTemplate, JdbcCourseDao courseDao, StudentMapper studentMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.courseDao = courseDao;
		this.studentMapper = studentMapper;
	}

	@Override
	public void create(Student student) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(CREATE_STUDENT_QUERY,
						new String[] { STUDENT_ID });
				statement.setObject(1,
						Optional.ofNullable(student).map(Student::getGroup).map(Group::getId).orElse(null));
				statement.setString(2, student.getName());
				statement.setString(3, student.getSurname());
				statement.setString(4, student.getPhone());
				statement.setString(5, student.getEmail());
				statement.setString(6, student.getAddress());
				statement.setObject(7, student.getBirthDate());
				statement.setString(8, student.getGender().toString());
				return statement;
			}, keyHolder);
			student.setId(keyHolder.getKey().longValue());
			student.getCourses()
					.stream()
					.forEach(c -> jdbcTemplate.update(CREATE_STUDENT_COURSE_QUERY, student.getId(), c.getId()));
		} catch (DataAccessException e) {
			throw new DaoException("Could not create student: " + student, e);
		}
	}

	@Override
	public Optional<Student> findById(Long studentId) {
		try {
			return Optional.of(
					jdbcTemplate.queryForObject(FIND_STUDENT_BY_ID_QUERY, new Object[] { studentId }, studentMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DaoException("Could not get student by id: " + studentId, e);
		}
	}

	@Override
	public List<Student> getAll() {
		try {
			return jdbcTemplate.query(GET_STUDENTS_QUERY, studentMapper);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get students", e);
		}
	}

	@Override
	public void update(Student student) {
		try {
			jdbcTemplate.update(UPDATE_STUDENT_QUERY,
					Optional.ofNullable(student).map(Student::getGroup).map(Group::getId).orElse(null),
					student.getName(),
					student.getSurname(),
					student.getPhone(),
					student.getEmail(),
					student.getAddress(),
					student.getBirthDate(),
					student.getGender().toString(),
					student.getId());
			List<Course> courses = courseDao.getByStudentId(student.getId());
			courses.stream()
					.filter(c -> !student.getCourses().contains(c))
					.forEach(c -> jdbcTemplate.update(DELETE_STUDENT_COURSE_QUERY, student.getId(), c.getId()));
			student.getCourses()
					.stream()
					.filter(c -> !courses.contains(c))
					.forEach(c -> jdbcTemplate.update(CREATE_STUDENT_COURSE_QUERY, student.getId(), c.getId()));
		} catch (DataAccessException e) {
			throw new DaoException("Could not update student: " + student, e);
		}
	}

	@Override
	public void deleteById(Long studentId) {
		try {
			jdbcTemplate.update(DELETE_STUDENT_BY_ID_QUERY, studentId);
		} catch (DataAccessException e) {
			throw new DaoException("Could not delete student by id: " + studentId, e);
		}
	}

	@Override
	public List<Student> getByGroup(Group group) {
		try {
			return jdbcTemplate.query(GET_STUDENTS_BY_GROUP_ID_QUERY, new Object[] { group.getId() }, (rs, rowNum) -> {
				Student student = new Student(rs.getString(STUDENT_NAME), rs.getString(STUDENT_SURNAME));
				student.setId(rs.getLong(STUDENT_ID));
				student.setPhone(rs.getString(STUDENT_PHONE));
				student.setEmail(rs.getString(STUDENT_EMAIL));
				student.setAddress(rs.getString(STUDENT_ADDRESS));
				student.setBirthDate(rs.getObject(STUDENT_BIRTH_DATE, LocalDate.class));
				student.setGender(Gender.valueOf(rs.getString(STUDENT_GENDER)));
				student.setGroup(group);
				student.setCourses(courseDao.getByStudentId(rs.getLong(STUDENT_ID)).stream().collect(toSet()));
				return student;
			});
		} catch (DataAccessException e) {
			throw new DaoException("Could not get students by group: " + group, e);
		}
	}

	@Override
	public List<Student> getByCourseId(Long courseId) {
		try {
			return jdbcTemplate.query(GET_STUDENTS_BY_COURSE_ID_QUERY, new Object[] { courseId }, studentMapper);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get students by course id: " + courseId, e);
		}
	}

	@Override
	public int count() {
		try {
			return jdbcTemplate.queryForObject(COUNT_STUDENTS_QUERY, Integer.class);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get amount of students", e);
		}
	}

	@Override
	public Page<Student> getAllPage(Pageable pageable) {
		try {
			List<Student> students = jdbcTemplate.query(
					GET_STUDENTS_ORDERED_BY_NAME_WITH_LIMIT_AND_OFFSET_QUERY,
					new Object[] { pageable.getPageSize(), pageable.getOffset() },
					studentMapper);
			return new PageImpl<>(students, pageable, count());
		} catch (DataAccessException e) {
			throw new DaoException("Could not get students", e);
		}
	}
}
