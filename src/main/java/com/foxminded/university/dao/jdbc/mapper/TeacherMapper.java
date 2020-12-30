package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;

public class TeacherMapper implements RowMapper<Teacher> {

	public static final String TEACHER_NAME = "teacher_name";
	public static final String TEACHER_SURNAME = "teacher_surname";
	public static final String TEACHER_RANK = "teacher_rank";
	public static final String TEACHER_PHONE = "teacher_phone";
	public static final String TEACHER_EMAIL = "teacher_email";
	public static final String TEACHER_ADDRESS = "teacher_address";
	public static final String TEACHER_BIRTHDATE = "teacher_birthdate";
	public static final String TEACHER_GENDER = "teacher_gender";
	public static final String TEACHER_ID = "teacher_id";

	@Override
	public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
		Teacher teacher = new Teacher(rs.getString(TEACHER_NAME), rs.getString(TEACHER_SURNAME));
		teacher.setId(rs.getLong(TEACHER_ID));
		teacher.setRank(rs.getString(TEACHER_RANK));
		teacher.setPhone(rs.getString(TEACHER_PHONE));
		teacher.setEmail(rs.getString(TEACHER_EMAIL));
		teacher.setAddress(rs.getString(TEACHER_ADDRESS));
		teacher.setBirthdate(rs.getObject(TEACHER_BIRTHDATE, LocalDate.class));
		teacher.setGender(Gender.valueOf(rs.getString(TEACHER_GENDER)));
		return teacher;
	}
}
