package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.model.Lesson;

public class LessonMapper implements RowMapper<Lesson> {

	private static final String LESSON_ID = "lesson_id";
	private static final String LESSON_DATE = "lesson_date";

	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lesson lesson = new Lesson();
		lesson.setId(rs.getLong(LESSON_ID));
		lesson.setDate(rs.getObject(LESSON_DATE, LocalDate.class));
		return lesson;
	}
}
