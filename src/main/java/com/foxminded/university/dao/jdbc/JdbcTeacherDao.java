package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.TeacherMapper.TEACHER_ID;

import java.sql.PreparedStatement;
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

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.exception.DaoException;
import com.foxminded.university.dao.jdbc.mapper.TeacherMapper;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

@Component
public class JdbcTeacherDao implements TeacherDao {

	private static final String CREATE_TEACHER_QUERY = "INSERT INTO teachers (name, surname, rank, phone, email, address, birth_date, gender) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String FIND_TEACHER_BY_ID_QUERY = "SELECT * FROM teachers WHERE id = ?";
	private static final String GET_TEACHERS_QUERY = "SELECT * FROM teachers";
	private static final String DELETE_TEACHER_BY_ID_QUERY = "DELETE FROM teachers WHERE id = ?";
	private static final String UPDATE_TEACHER_QUERY = "UPDATE teachers SET name = ?, surname = ?, rank = ?, phone = ?, email = ?, address = ?, birth_date = ?, gender = ? "
			+ "WHERE id = ?";
	private static final String CREATE_TEACHER_COURSE_QUERY = "INSERT INTO teachers_courses (teacher_id, course_id) VALUES(?, ?)";
	private static final String DELETE_TEACHER_COURSE_QUERY = "DELETE FROM teachers_courses WHERE teacher_id = ? AND course_id =?";
	private static final String GET_TEACHERS_BY_COURSE_ID_QUERY = "SELECT * FROM teachers "
			+ "JOIN teachers_courses ON teachers_courses.teacher_id = teachers.id WHERE course_id = ?";
	private static final String COUNT_TEACHERS_QUERY = "SELECT count(*) FROM teachers";
	private static final String GET_TEACHERS_ORDERED_BY_NAME_WITH_LIMIT_AND_OFFSET_QUERY = "SELECT * FROM teachers ORDER BY name, surname LIMIT ? OFFSET ?";

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
		try {
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
		} catch (DataAccessException e) {
			throw new DaoException("Could not create teacher: " + teacher, e);
		}
	}

	@Override
	public Optional<Teacher> findById(Long teacherId) {
		try {
			return Optional.of(
					jdbcTemplate.queryForObject(FIND_TEACHER_BY_ID_QUERY, new Object[] { teacherId }, teacherMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DaoException("Could not get teacher by id: " + teacherId, e);
		}
	}

	@Override
	public List<Teacher> getAll() {
		try {
			return jdbcTemplate.query(GET_TEACHERS_QUERY, teacherMapper);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get teachers", e);
		}
	}

	@Override
	public void update(Teacher teacher) {
		try {
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
			List<Course> courses = courseDao.getByTeacherId(teacher.getId());
			courses.stream()
					.filter(c -> !teacher.getCourses().contains(c))
					.forEach(c -> jdbcTemplate.update(DELETE_TEACHER_COURSE_QUERY, teacher.getId(), c.getId()));
			teacher.getCourses()
					.stream()
					.filter(c -> !courses.contains(c))
					.forEach(c -> jdbcTemplate.update(CREATE_TEACHER_COURSE_QUERY, teacher.getId(), c.getId()));
		} catch (DataAccessException e) {
			throw new DaoException("Could not update teacher: " + teacher, e);
		}
	}

	@Override
	public void deleteById(Long teacherId) {
		try {
			jdbcTemplate.update(DELETE_TEACHER_BY_ID_QUERY, teacherId);
		} catch (DataAccessException e) {
			throw new DaoException("Could not delete teacher by id: " + teacherId, e);
		}
	}

	@Override
	public List<Teacher> getByCourseId(Long courseId) {
		try {
			return jdbcTemplate.query(GET_TEACHERS_BY_COURSE_ID_QUERY, new Object[] { courseId }, teacherMapper);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get teachers by course id: " + courseId, e);
		}
	}

	@Override
	public int count() {
		try {
			return jdbcTemplate.queryForObject(COUNT_TEACHERS_QUERY, Integer.class);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get amount of teachers", e);
		}
	}

	@Override
	public Page<Teacher> getAllPage(Pageable pageable) {
		try {
			List<Teacher> teachers = jdbcTemplate.query(
					GET_TEACHERS_ORDERED_BY_NAME_WITH_LIMIT_AND_OFFSET_QUERY,
					new Object[] { pageable.getPageSize(), pageable.getOffset() },
					teacherMapper);
			return new PageImpl<>(teachers, pageable, count());
		} catch (DataAccessException e) {
			throw new DaoException("Could not get teachers", e);
		}
	}
}
