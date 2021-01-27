package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Room;

public interface RoomDao extends GenericDao<Room> {

	public List<Room> getRoomsByCourseId(Long courseId);

	public Optional<Room> findByName(String name);
}
