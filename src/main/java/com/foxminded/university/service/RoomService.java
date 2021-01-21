package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;

@Service
public class RoomService {

	private RoomDao roomDao;

	public RoomService(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	@Transactional
	public void create(Room room) {
		roomDao.create(room);
	}

	@Transactional
	public Room findById(Long id) {
		return roomDao.findById(id);
	}

	@Transactional
	public List<Room> getAll() {
		return roomDao.getAll();
	}

	@Transactional
	public void update(Room room) {
		roomDao.update(room);
	}

	@Transactional
	public void deleteById(Long id) {
		roomDao.deleteById(id);
	}

	@Transactional
	public List<Room> getRoomsByCourseId(Long courseId) {
		return roomDao.getRoomsByCourseId(courseId);
	}
}
