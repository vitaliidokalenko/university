package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Room;

public class RoomRepository {

	List<Room> rooms = new ArrayList<>();

	public void create(Room room) {
		rooms.add(room);
	}

	public void delete(Room room) {
		rooms.remove(room);
	}

	public List<Room> getRooms() {
		return rooms;
	}
}
