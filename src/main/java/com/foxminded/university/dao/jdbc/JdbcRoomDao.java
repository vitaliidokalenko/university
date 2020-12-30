package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.RoomMapper.ROOM_ID;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.jdbc.mapper.RoomMapper;
import com.foxminded.university.model.Room;

@Component
public class JdbcRoomDao implements RoomDao {

	private static final String CREATE_ROOM_QUERY = "INSERT INTO rooms (room_name, room_capacity) VALUES (?, ?);";
	private static final String DELETE_ROOM_BY_ID_QUERY = "DELETE FROM rooms WHERE room_id = ?;";
	private static final String FIND_ROOM_BY_ID_QUERY = "SELECT * FROM rooms WHERE room_id = ?";
	private static final String GET_ROOMS_QUERY = "SELECT * FROM rooms;";
	private static final String UPDATE_ROOM_QUERY = "UPDATE rooms SET room_name = ?, room_capacity = ? WHERE room_id = ?;";
	private static final String GET_ROOMS_BY_COURSE_ID_QUERY = "SELECT * FROM rooms "
			+ "JOIN courses_rooms ON courses_rooms.room_id = rooms.room_id WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;

	public JdbcRoomDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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
	public Room findById(Long roomId) {
		return jdbcTemplate.queryForObject(FIND_ROOM_BY_ID_QUERY, new Object[] { roomId }, new RoomMapper());
	}

	@Override
	public List<Room> getAll() {
		return jdbcTemplate.query(GET_ROOMS_QUERY, new RoomMapper());
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
	public List<Room> getRoomsByCourseId(Long courseId) {
		return jdbcTemplate.query(GET_ROOMS_BY_COURSE_ID_QUERY, new Object[] { courseId }, new RoomMapper());
	}
}
