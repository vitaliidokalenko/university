package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.RoomMapper.ROOM_CAPACITY;
import static com.foxminded.university.dao.jdbc.mapper.RoomMapper.ROOM_ID;
import static com.foxminded.university.dao.jdbc.mapper.RoomMapper.ROOM_NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.jdbc.mapper.RoomMapper;
import com.foxminded.university.model.Room;

@Component
public class JdbcRoomDao implements RoomDao {

	private static final String ROOMS_TABLE_NAME = "rooms";
	private static final String DELETE_ROOM_BY_ID_QUERY = "DELETE FROM rooms WHERE room_id = ?;";
	private static final String FIND_ROOM_BY_ID_QUERY = "SELECT * FROM rooms WHERE room_id = ?";
	private static final String GET_ROOMS_QUERY = "SELECT * FROM rooms;";
	private static final String UPDATE_ROOM_QUERY = "UPDATE rooms SET room_name = ?, room_capacity = ? WHERE room_id = ?;";
	private static final String GET_ROOMS_BY_COURSE_ID_QUERY = "SELECT * FROM rooms "
			+ "JOIN courses_rooms ON courses_rooms.room_id = rooms.room_id WHERE course_id = ?;";

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;

	public JdbcRoomDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(ROOMS_TABLE_NAME)
				.usingGeneratedKeyColumns(ROOM_ID);
	}

	@Override
	public void create(Room room) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(ROOM_NAME, room.getName());
		parameters.put(ROOM_CAPACITY, room.getCapacity());
		room.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());
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
