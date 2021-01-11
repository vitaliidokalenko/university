package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Room;

public interface RoomService extends GenericService<Room> {

	public List<Room> getRoomsByCourseId(Long courseId);
}
