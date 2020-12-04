package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.foxminded.university.model.Room;

public class RoomRepository {

	List<Room> rooms = new ArrayList<>();
	AtomicInteger id = new AtomicInteger(1);

	public void create(Room room) {
		room.setId(id.getAndIncrement());
		rooms.add(room);
	}

	public void deleteById(int id) {
		rooms.removeIf(r -> r.getId() == id);
	}

	public List<Room> getRooms() {
		return rooms;
	}
}
