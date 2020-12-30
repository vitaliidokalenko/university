package com.foxminded.university.dao.jdbc.mapper;

import static com.foxminded.university.dao.jdbc.mapper.GroupMapper.GROUP_ID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;

public class StudentMapper implements RowMapper<Student> {

	public static final String STUDENT_NAME = "student_name";
	public static final String STUDENT_SURNAME = "student_surname";
	public static final String STUDENT_ID = "student_id";
	public static final String STUDENT_PHONE = "student_phone";
	public static final String STUDENT_EMAIL = "student_email";
	public static final String STUDENT_ADDRESS = "student_address";
	public static final String STUDENT_BIRTHDATE = "student_birthdate";
	public static final String STUDENT_GENDER = "student_gender";

	private JdbcGroupDao groupDao;

	public StudentMapper(JdbcGroupDao groupDao) {
		this.groupDao = groupDao;
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
		student.setGroup(groupDao.findById(rs.getLong(GROUP_ID)));
		return student;
	}
}
