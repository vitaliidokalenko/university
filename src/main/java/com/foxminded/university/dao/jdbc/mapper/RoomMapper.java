package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.model.Room;

public class RoomMapper implements RowMapper<Room> {

	public static final String ROOM_ID = "id";
	public static final String ROOM_NAME = "name";
	public static final String ROOM_CAPACITY = "capacity";

	@Override
	public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
		Room room = new Room(rs.getString(ROOM_NAME));
		room.setId(rs.getLong(ROOM_ID));
		room.setCapacity(rs.getInt(ROOM_CAPACITY));
		return room;
	}
}
