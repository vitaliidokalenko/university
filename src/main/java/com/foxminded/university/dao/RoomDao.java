package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Room;

public interface RoomDao {

	public void create(Room room);

	public Room findById(Long roomId);

	public List<Room> getAll();

	public void update(Room room);

	public void deleteById(Long roomId);

	public List<Room> getRoomsByCourseId(Long courseId);
}
