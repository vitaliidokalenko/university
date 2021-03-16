package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.RoomMapper.ROOM_ID;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.exception.DaoException;
import com.foxminded.university.dao.jdbc.mapper.RoomMapper;
import com.foxminded.university.model.Room;

@Component
public class JdbcRoomDao implements RoomDao {

	private static final String CREATE_ROOM_QUERY = "INSERT INTO rooms (name, capacity) VALUES (?, ?)";
	private static final String DELETE_ROOM_BY_ID_QUERY = "DELETE FROM rooms WHERE id = ?";
	private static final String FIND_ROOM_BY_ID_QUERY = "SELECT * FROM rooms WHERE id = ?";
	private static final String GET_ROOMS_QUERY = "SELECT * FROM rooms";
	private static final String UPDATE_ROOM_QUERY = "UPDATE rooms SET name = ?, capacity = ? WHERE id = ?";
	private static final String GET_ROOMS_BY_COURSE_ID_QUERY = "SELECT * FROM rooms "
			+ "JOIN courses_rooms ON courses_rooms.room_id = rooms.id WHERE course_id = ?";
	private static final String FIND_ROOM_BY_NAME_QUERY = "SELECT * FROM rooms WHERE name = ?";
	private static final String COUNT_ROOMS_QUERY = "SELECT count(*) FROM rooms";
	private static final String GET_ROOMS_ORDERED_BY_NAME_WITH_LIMIT_AND_OFFSET_QUERY = "SELECT * FROM rooms ORDER BY name LIMIT ? OFFSET ?";

	private JdbcTemplate jdbcTemplate;
	private RoomMapper roomMapper;

	public JdbcRoomDao(JdbcTemplate jdbcTemplate, RoomMapper roomMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.roomMapper = roomMapper;
	}

	@Override
	public void create(Room room) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(CREATE_ROOM_QUERY, new String[] { ROOM_ID });
				statement.setString(1, room.getName());
				statement.setInt(2, room.getCapacity());
				return statement;
			}, keyHolder);
			room.setId(keyHolder.getKey().longValue());
		} catch (DataAccessException e) {
			throw new DaoException("Could not create room: " + room, e);
		}
	}

	@Override
	public Optional<Room> findById(Long roomId) {
		try {
			return Optional.of(jdbcTemplate.queryForObject(FIND_ROOM_BY_ID_QUERY, new Object[] { roomId }, roomMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DaoException("Could not get room by id: " + roomId, e);
		}
	}

	@Override
	public List<Room> getAll() {
		try {
			return jdbcTemplate.query(GET_ROOMS_QUERY, roomMapper);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get rooms", e);
		}
	}

	@Override
	public void update(Room room) {
		try {
			jdbcTemplate.update(UPDATE_ROOM_QUERY, room.getName(), room.getCapacity(), room.getId());
		} catch (DataAccessException e) {
			throw new DaoException("Cold not update room: " + room, e);
		}
	}

	@Override
	public void deleteById(Long roomId) {
		try {
			jdbcTemplate.update(DELETE_ROOM_BY_ID_QUERY, roomId);
		} catch (DataAccessException e) {
			throw new DaoException("Could not delete room by id: " + roomId, e);
		}
	}

	@Override
	public List<Room> getByCourseId(Long courseId) {
		try {
			return jdbcTemplate.query(GET_ROOMS_BY_COURSE_ID_QUERY, new Object[] { courseId }, roomMapper);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get rooms by course id: " + courseId, e);
		}
	}

	@Override
	public Optional<Room> findByName(String name) {
		try {
			return Optional.of(jdbcTemplate.queryForObject(FIND_ROOM_BY_NAME_QUERY, new Object[] { name }, roomMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DaoException("Could not get room by name: " + name, e);
		}
	}

	@Override
	public int count() {
		try {
			return jdbcTemplate.queryForObject(COUNT_ROOMS_QUERY, Integer.class);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get amount of rooms", e);
		}
	}

	@Override
	public Page<Room> getAllPage(Pageable pageable) {
		try {
			List<Room> rooms = jdbcTemplate.query(
					GET_ROOMS_ORDERED_BY_NAME_WITH_LIMIT_AND_OFFSET_QUERY,
					new Object[] { pageable.getPageSize(), pageable.getOffset() },
					roomMapper);
			return new PageImpl<>(rooms, pageable, count());
		} catch (DataAccessException e) {
			throw new DaoException("Could not get rooms", e);
		}
	}
}
