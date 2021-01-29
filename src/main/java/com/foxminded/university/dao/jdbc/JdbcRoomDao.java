package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.RoomMapper.ROOM_ID;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.jdbc.mapper.RoomMapper;
import com.foxminded.university.model.Room;

@Component
public class JdbcRoomDao implements RoomDao {

	private static final String CREATE_ROOM_QUERY = "INSERT INTO rooms (name, capacity) VALUES (?, ?);";
	private static final String DELETE_ROOM_BY_ID_QUERY = "DELETE FROM rooms WHERE id = ?;";
	private static final String FIND_ROOM_BY_ID_QUERY = "SELECT * FROM rooms WHERE id = ?;";
	private static final String GET_ROOMS_QUERY = "SELECT * FROM rooms;";
	private static final String UPDATE_ROOM_QUERY = "UPDATE rooms SET name = ?, capacity = ? WHERE id = ?;";
	private static final String GET_ROOMS_BY_COURSE_ID_QUERY = "SELECT * FROM rooms "
			+ "JOIN courses_rooms ON courses_rooms.room_id = rooms.id WHERE course_id = ?;";
	private static final String FIND_ROOM_BY_NAME_QUERY = "SELECT * FROM rooms WHERE name = ?;";

	private JdbcTemplate jdbcTemplate;
	private RoomMapper roomMapper;

	public JdbcRoomDao(JdbcTemplate jdbcTemplate, RoomMapper roomMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.roomMapper = roomMapper;
	}

	@Override
	public void create(Room room) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(CREATE_ROOM_QUERY, new String[] { ROOM_ID });
			statement.setString(1, room.getName());
			statement.setInt(2, room.getCapacity());
			return statement;
		}, keyHolder);
		room.setId(keyHolder.getKey().longValue());
	}

	@Override
	public Optional<Room> findById(Long roomId) {
		try {
			return Optional.of(jdbcTemplate.queryForObject(FIND_ROOM_BY_ID_QUERY, new Object[] { roomId }, roomMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Room> getAll() {
		return jdbcTemplate.query(GET_ROOMS_QUERY, roomMapper);
	}

	@Override
	public void update(Room room) {
		jdbcTemplate.update(UPDATE_ROOM_QUERY, room.getName(), room.getCapacity(), room.getId());
	}

	@Override
	public void deleteById(Long roomId) {
		jdbcTemplate.update(DELETE_ROOM_BY_ID_QUERY, roomId);
	}

	@Override
	public List<Room> getByCourseId(Long courseId) {
		return jdbcTemplate.query(GET_ROOMS_BY_COURSE_ID_QUERY, new Object[] { courseId }, roomMapper);
	}

	@Override
	public Optional<Room> findByName(String name) {
		try {
			return Optional.of(jdbcTemplate.queryForObject(FIND_ROOM_BY_NAME_QUERY, new Object[] { name }, roomMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}
}
