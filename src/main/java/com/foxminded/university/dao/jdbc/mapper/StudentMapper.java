package com.foxminded.university.dao.jdbc.mapper;

import static java.util.stream.Collectors.toSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;

@Component
public class StudentMapper implements RowMapper<Student> {

	public static final String STUDENT_ID = "id";
	public static final String GROUP_ID = "group_id";
	public static final String STUDENT_NAME = "name";
	public static final String STUDENT_SURNAME = "surname";
	public static final String STUDENT_PHONE = "phone";
	public static final String STUDENT_EMAIL = "email";
	public static final String STUDENT_ADDRESS = "address";
	public static final String STUDENT_BIRTH_DATE = "birth_date";
	public static final String STUDENT_GENDER = "gender";

	private JdbcGroupDao groupDao;
	private JdbcCourseDao courseDao;

	public StudentMapper(JdbcGroupDao groupDao, JdbcCourseDao courseDao) {
		this.groupDao = groupDao;
		this.courseDao = courseDao;
	}

	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student student = new Student(rs.getString(STUDENT_NAME), rs.getString(STUDENT_SURNAME));
		student.setId(rs.getLong(STUDENT_ID));
		student.setPhone(rs.getString(STUDENT_PHONE));
		student.setEmail(rs.getString(STUDENT_EMAIL));
		student.setAddress(rs.getString(STUDENT_ADDRESS));
		student.setBirthDate(rs.getObject(STUDENT_BIRTH_DATE, LocalDate.class));
		student.setGender(Gender.valueOf(rs.getString(STUDENT_GENDER)));
		student.setGroup(groupDao.findById(rs.getLong(GROUP_ID)).orElse(null));
		student.setCourses(courseDao.getCoursesByStudentId(rs.getLong(STUDENT_ID)).stream().collect(toSet()));
		return student;
	}
}
