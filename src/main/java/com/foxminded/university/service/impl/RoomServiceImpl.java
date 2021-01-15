package com.foxminded.university.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

	private RoomDao roomDao;

	public RoomServiceImpl(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	@Override
	@Transactional
	public void create(Room room) {
		roomDao.create(room);
	}

	@Override
	@Transactional
	public Room findById(Long id) {
		return roomDao.findById(id);
	}

	@Override
	@Transactional
	public List<Room> getAll() {
		return roomDao.getAll();
	}

	@Override
	@Transactional
	public void update(Room room) {
		roomDao.update(room);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		roomDao.deleteById(id);
	}

	@Override
	@Transactional
	public List<Room> getRoomsByCourseId(Long courseId) {
		return roomDao.getRoomsByCourseId(courseId);
	}
}
