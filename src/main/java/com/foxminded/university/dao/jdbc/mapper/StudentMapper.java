package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;

public class StudentMapper implements RowMapper<Student> {

	private static final String STUDENT_NAME = "student_name";
	private static final String STUDENT_SURNAME = "student_surname";
	private static final String STUDENT_ID = "student_id";
	private static final String STUDENT_PHONE = "student_phone";
	private static final String STUDENT_EMAIL = "student_email";
	private static final String STUDENT_ADDRESS = "student_address";
	private static final String STUDENT_BIRTHDATE = "student_birthdate";
	private static final String STUDENT_GENDER = "student_gender";
	private static final String GROUP_ID = "group_id";

	private JdbcGroupDao jdbcGroupDao;

	public StudentMapper(JdbcGroupDao jdbcGroupDao) {
		this.jdbcGroupDao = jdbcGroupDao;
	}

	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student student = new Student(rs.getString(STUDENT_NAME), rs.getString(STUDENT_SURNAME));
		student.setId(rs.getLong(STUDENT_ID));
		student.setPhone(rs.getString(STUDENT_PHONE));
		student.setEmail(rs.getString(STUDENT_EMAIL));
		student.setAddress(rs.getString(STUDENT_ADDRESS));
		student.setBirthdate(rs.getObject(STUDENT_BIRTHDATE, LocalDate.class));
		student.setGender(Gender.valueOf(rs.getString(STUDENT_GENDER)));
		student.setGroup(jdbcGroupDao.findById(rs.getLong(GROUP_ID)));
		return student;
	}
}
