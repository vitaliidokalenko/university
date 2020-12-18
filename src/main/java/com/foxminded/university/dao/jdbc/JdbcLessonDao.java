package com.foxminded.university.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.jdbc.mapper.LessonMapper;
import com.foxminded.university.model.Lesson;

@Component
public class JdbcLessonDao implements LessonDao {

	private static final String LESSONS_TABLE_NAME = "lessons";
	private static final String LESSON_ID = "lesson_id";
	private static final String LESSON_DATE = "lesson_date";
	private static final String COURSE_ID = "course_id";
	private static final String TIMEFRAME_ID = "timeframe_id";
	private static final String TEACHER_ID = "teacher_id";
	private static final String ROOM_ID = "room_id";
	private static final String FIND_LESSON_BY_ID_QUERY = "SELECT * FROM lessons WHERE lesson_id = ?;";
	private static final String GET_LESSONS_QUERY = "SELECT * FROM lessons";
	private static final String DELETE_LESSON_BY_ID_QUERY = "DELETE FROM lessons WHERE lesson_id = ?";
	private static final String UPDATE_LESSON_QUERY = "UPDATE lessons SET lesson_date = ?, timeframe_id = ?, course_id = ?, teacher_id = ?, room_id = ? "
			+ "WHERE lesson_id = ?";
	private static final String CREATE_LESSON_GROUP_QUERY = "INSERT INTO lessons_groups (lesson_id, group_id) VALUES(?, ?);";
	private static final String DELETE_LESSON_GROUP_QUERY = "DELETE FROM lessons_groups WHERE lesson_id = ? AND group_id =?;";
	private static final String GET_LESSONS_BY_GROUP_ID_QUERY = "SELECT * FROM lessons "
			+ "JOIN lessons_groups ON lessons_groups.lesson_id = lessons.lesson_id WHERE group_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;

	@Autowired
	public JdbcLessonDao(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(LESSONS_TABLE_NAME)
				.usingGeneratedKeyColumns(LESSON_ID);
	}

	@Override
	public void create(Lesson lesson) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(LESSON_DATE, lesson.getDate());
		parameters.put(TIMEFRAME_ID, lesson.getTimeframe().getId());
		parameters.put(COURSE_ID, lesson.getCourse().getId());
		parameters.put(TEACHER_ID, lesson.getTeacher().getId());
		parameters.put(ROOM_ID, lesson.getRoom().getId());
		lesson.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());
	}

	@Override
	public Lesson findById(Long lessonId) {
		return jdbcTemplate.queryForObject(FIND_LESSON_BY_ID_QUERY, new Object[] { lessonId }, new LessonMapper());
	}

	@Override
	public List<Lesson> getAll() {
		return jdbcTemplate.query(GET_LESSONS_QUERY, new LessonMapper());
	}

	@Override
	public void update(Lesson lesson) {
		jdbcTemplate.update(UPDATE_LESSON_QUERY,
				lesson.getDate(),
				lesson.getTimeframe().getId(),
				lesson.getCourse().getId(),
				lesson.getTeacher().getId(),
				lesson.getRoom().getId(),
				lesson.getId());
	}

	@Override
	public void deleteById(Long lessonId) {
		jdbcTemplate.update(DELETE_LESSON_BY_ID_QUERY, lessonId);
	}

	@Override
	public void createLessonsGroups(Long lessonId, Long groupId) {
		jdbcTemplate.update(CREATE_LESSON_GROUP_QUERY, lessonId, groupId);

	}

	@Override
	public void deleteLessonsGroups(Long lessonId, Long groupId) {
		jdbcTemplate.update(DELETE_LESSON_GROUP_QUERY, lessonId, groupId);
	}

	@Override
	public List<Lesson> getLessonsByGroupId(Long groupId) {
		return jdbcTemplate.query(GET_LESSONS_BY_GROUP_ID_QUERY, new Object[] { groupId }, new LessonMapper());
	}
}
