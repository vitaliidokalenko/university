package com.foxminded.university.dao.jdbc.mapper;

import static java.util.stream.Collectors.toSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

	public static final String TEACHER_ID = "id";
	public static final String TEACHER_NAME = "name";
	public static final String TEACHER_SURNAME = "surname";
	public static final String TEACHER_RANK = "rank";
	public static final String TEACHER_PHONE = "phone";
	public static final String TEACHER_EMAIL = "email";
	public static final String TEACHER_ADDRESS = "address";
	public static final String TEACHER_BIRTH_DATE = "birth_date";
	public static final String TEACHER_GENDER = "gender";

	private JdbcCourseDao courseDao;

	public TeacherMapper(JdbcCourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Override
	public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
		Teacher teacher = new Teacher(rs.getString(TEACHER_NAME), rs.getString(TEACHER_SURNAME));
		teacher.setId(rs.getLong(TEACHER_ID));
		teacher.setRank(rs.getString(TEACHER_RANK));
		teacher.setPhone(rs.getString(TEACHER_PHONE));
		teacher.setEmail(rs.getString(TEACHER_EMAIL));
		teacher.setAddress(rs.getString(TEACHER_ADDRESS));
		teacher.setBirthDate(rs.getObject(TEACHER_BIRTH_DATE, LocalDate.class));
		teacher.setGender(Gender.valueOf(rs.getString(TEACHER_GENDER)));
		teacher.setCourses(courseDao.getByTeacherId(rs.getLong(TEACHER_ID)).stream().collect(toSet()));
		return teacher;
	}
}
