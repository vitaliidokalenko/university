package com.foxminded.university.dao.jdbc.mapper;

import static java.util.stream.Collectors.toSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.dao.jdbc.JdbcRoomDao;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.dao.jdbc.JdbcTimeframeDao;
import com.foxminded.university.model.Lesson;

@Component
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
	private JdbcGroupDao groupDao;

	public LessonMapper(JdbcTimeframeDao timeframeDao, JdbcCourseDao courseDao, JdbcTeacherDao teacherDao,
			JdbcRoomDao roomDao, JdbcGroupDao groupDao) {
		this.timeframeDao = timeframeDao;
		this.courseDao = courseDao;
		this.teacherDao = teacherDao;
		this.roomDao = roomDao;
		this.groupDao = groupDao;
	}

	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lesson lesson = new Lesson();
		lesson.setId(rs.getLong(LESSON_ID));
		lesson.setDate(rs.getObject(LESSON_DATE, LocalDate.class));
		lesson.setTimeframe(timeframeDao.findById(rs.getLong(TIMEFRAME_ID)).orElse(null));
		lesson.setCourse(courseDao.findById(rs.getLong(COURSE_ID)).orElse(null));
		lesson.setTeacher(teacherDao.findById(rs.getLong(TEACHER_ID)).orElse(null));
		lesson.setRoom(roomDao.findById(rs.getLong(ROOM_ID)).orElse(null));
		lesson.setGroups(groupDao.getGroupsByLessonId(rs.getLong(LESSON_ID)).stream().collect(toSet()));
		return lesson;
	}
}
