package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.GroupMapper.GROUP_ID;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.exception.DAOException;
import com.foxminded.university.dao.jdbc.mapper.GroupMapper;
import com.foxminded.university.model.Group;

@Component
public class JdbcGroupDao implements GroupDao {

	private static final String CREATE_GROUP_QUERY = "INSERT INTO groups (name) VALUES (?)";
	private static final String DELETE_GROUP_BY_ID_QUERY = "DELETE FROM groups WHERE id = ?";
	private static final String FIND_GROUP_BY_ID_QUERY = "SELECT * FROM groups WHERE id = ?";
	private static final String GET_GROUPS_QUERY = "SELECT * FROM groups";
	private static final String UPDATE_GROUP_QUERY = "UPDATE groups SET name = ? WHERE id = ?";
	private static final String GET_GROUPS_BY_LESSON_ID_QUERY = "SELECT * FROM groups "
			+ "JOIN lessons_groups ON lessons_groups.group_id = groups.id WHERE lesson_id = ?";
	private static final String FIND_GROUP_BY_NAME_QUERY = "SELECT * FROM groups WHERE name = ?";

	private JdbcTemplate jdbcTemplate;
	private GroupMapper groupMapper;

	public JdbcGroupDao(JdbcTemplate jdbcTemplate, GroupMapper groupMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.groupMapper = groupMapper;
	}

	@Override
	public void create(Group group) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(CREATE_GROUP_QUERY,
						new String[] { GROUP_ID });
				statement.setString(1, group.getName());
				return statement;
			}, keyHolder);
			group.setId(keyHolder.getKey().longValue());
		} catch (DataAccessException e) {
			throw new DAOException("Could not create group: " + group, e);
		}
	}

	@Override
	public Optional<Group> findById(Long groupId) {
		try {
			return Optional
					.of(jdbcTemplate.queryForObject(FIND_GROUP_BY_ID_QUERY, new Object[] { groupId }, groupMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DAOException("Could not get group by id: " + groupId, e);
		}
	}

	@Override
	public List<Group> getAll() {
		try {
			return jdbcTemplate.query(GET_GROUPS_QUERY, groupMapper);
		} catch (DataAccessException e) {
			throw new DAOException("Could not get groups", e);
		}
	}

	@Override
	public void update(Group group) {
		try {
			jdbcTemplate.update(UPDATE_GROUP_QUERY, group.getName(), group.getId());
		} catch (DataAccessException e) {
			throw new DAOException("Could not update group: " + group, e);
		}
	}

	@Override
	public void deleteById(Long groupId) {
		try {
			jdbcTemplate.update(DELETE_GROUP_BY_ID_QUERY, groupId);
		} catch (DataAccessException e) {
			throw new DAOException("Could not delete group by id: " + groupId, e);
		}
	}

	@Override
	public List<Group> getByLessonId(Long lessonId) {
		try {
			return jdbcTemplate.query(GET_GROUPS_BY_LESSON_ID_QUERY, new Object[] { lessonId }, groupMapper);
		} catch (DataAccessException e) {
			throw new DAOException("Could not get groups by lesson id: " + lessonId, e);
		}
	}

	@Override
	public Optional<Group> findByName(String name) {
		try {
			return Optional
					.of(jdbcTemplate.queryForObject(FIND_GROUP_BY_NAME_QUERY, new Object[] { name }, groupMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DAOException("Could not get group by name: " + name, e);
		}
	}
}
