package com.foxminded.university.controller.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.foxminded.university.model.Room;

public class RoomFormatter implements Formatter<Room> {

	@Override
	public String print(Room room, Locale locale) {
		return Long.toString(room.getId());
	}

	@Override
	public Room parse(String id, Locale locale) throws ParseException {
		Room room = new Room();
		room.setId(Long.valueOf(id));
		return room;
	}

}
