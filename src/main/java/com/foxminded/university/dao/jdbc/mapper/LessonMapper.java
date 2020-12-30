package com.foxminded.university.dao.jdbc.mapper;

import static com.foxminded.university.dao.jdbc.mapper.CourseMapper.COURSE_ID;
import static com.foxminded.university.dao.jdbc.mapper.RoomMapper.ROOM_ID;
import static com.foxminded.university.dao.jdbc.mapper.TeacherMapper.TEACHER_ID;
import static com.foxminded.university.dao.jdbc.mapper.TimeframeMapper.TIMEFRAME_ID;

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

	public static final String LESSON_ID = "lesson_id";
	public static final String LESSON_DATE = "lesson_date";

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
