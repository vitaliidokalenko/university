package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.dao.jdbc.JdbcRoomDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.dao.jdbc.JdbcTimeframeDao;
import com.foxminded.university.model.Lesson;

public class LessonMapper implements RowMapper<Lesson> {

	public static final String LESSON_ID = "id";
	public static final String LESSON_DATE = "date";
	public static final String COURSE_ID = "course_id";
	public static final String ROOM_ID = "room_id";
	public static final String TEACHER_ID = "teacher_id";
	public static final String TIMEFRAME_ID = "timeframe_id";

	private JdbcTimeframeDao timeframeDao;
	private JdbcCourseDao courseDao;
	private JdbcTeacherDao teacherDao;
	private JdbcRoomDao roomDao;

	public LessonMapper(JdbcTimeframeDao timeframeDao, JdbcCourseDao courseDao, JdbcTeacherDao teacherDao,
			JdbcRoomDao roomDao) {
		this.timeframeDao = timeframeDao;
		this.courseDao = courseDao;
		this.teacherDao = teacherDao;
		this.roomDao = roomDao;
	}

	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lesson lesson = new Lesson();
		lesson.setId(rs.getLong(LESSON_ID));
		lesson.setDate(rs.getObject(LESSON_DATE, LocalDate.class));
		lesson.setTimeframe(timeframeDao.findById(rs.getLong(TIMEFRAME_ID)));
		lesson.setCourse(courseDao.findById(rs.getLong(COURSE_ID)));
		lesson.setTeacher(teacherDao.findById(rs.getLong(TEACHER_ID)));
		lesson.setRoom(roomDao.findById(rs.getLong(ROOM_ID)));
		return lesson;
	}
}
