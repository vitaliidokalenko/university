package com.foxminded.university.dao.jdbc.mapper;

import static java.util.stream.Collectors.toSet;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcRoomDao;
import com.foxminded.university.model.Course;

@Component
public class CourseMapper implements RowMapper<Course> {

	public static final String COURSE_ID = "id";
	public static final String COURSE_NAME = "name";
	public static final String COURSE_DESCRIPTION = "description";

	private JdbcRoomDao roomDao;

	public CourseMapper(JdbcRoomDao roomDao) {
		this.roomDao = roomDao;
	}

	@Override
	public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
		Course course = new Course();
		course.setName(rs.getString(COURSE_NAME));
		course.setId(rs.getLong(COURSE_ID));
		course.setDescription(rs.getString(COURSE_DESCRIPTION));
		course.setRooms(roomDao.getByCourseId(rs.getLong(COURSE_ID)).stream().collect(toSet()));
		return course;
	}
}
