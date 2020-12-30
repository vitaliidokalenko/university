package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.GroupMapper.GROUP_ID;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import com.foxminded.university.model.Group;

@Component
public class JdbcGroupDao implements GroupDao {

	private static final String CREATE_GROUP_QUERY = "INSERT INTO groups (group_name) VALUES (?);";
	private static final String DELETE_GROUP_BY_ID_QUERY = "DELETE FROM groups WHERE group_id = ?;";
	private static final String FIND_GROUP_BY_ID_QUERY = "SELECT * FROM groups WHERE group_id = ?";
	private static final String GET_GROUPS_QUERY = "SELECT * FROM groups;";
	private static final String UPDATE_GROUP_QUERY = "UPDATE groups SET group_name = ? WHERE group_id = ?;";
	private static final String GET_GROUPS_BY_LESSON_ID_QUERY = "SELECT * FROM groups "
			+ "JOIN lessons_groups ON lessons_groups.group_id = groups.group_id WHERE lesson_id = ?;";

	private JdbcTemplate jdbcTemplate;

	public JdbcGroupDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void create(Group group) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(CREATE_GROUP_QUERY, new String[] { GROUP_ID });
			statement.setString(1, group.getName());
			return statement;
		}, keyHolder);
		group.setId(keyHolder.getKey().longValue());
	}

	@Override
	public Group findById(Long groupId) {
		if (groupId == 0) {
			return null;
		}
		return jdbcTemplate.queryForObject(FIND_GROUP_BY_ID_QUERY, new Object[] { groupId }, new GroupMapper());
	}

	@Override
	public List<Group> getAll() {
		return jdbcTemplate.query(GET_GROUPS_QUERY, new GroupMapper());
	}

	@Override
	public void update(Group group) {
		jdbcTemplate.update(UPDATE_GROUP_QUERY, group.getName(), group.getId());
	}

	@Override
	public void deleteById(Long groupId) {
		jdbcTemplate.update(DELETE_GROUP_BY_ID_QUERY, groupId);
	}

	@Override
	public List<Group> getGroupsByLessonId(Long lessonId) {
		return jdbcTemplate.query(GET_GROUPS_BY_LESSON_ID_QUERY, new Object[] { lessonId }, new GroupMapper());
	}
}
