package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

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
		if (isRoomValid(room)) {
			roomDao.create(room);
		}
	}

	@Transactional
	public Optional<Room> findById(Long id) {
		return roomDao.findById(id);
	}

	@Transactional
	public List<Room> getAll() {
		return roomDao.getAll();
	}

	@Transactional
	public void update(Room room) {
		if (isRoomValid(room)) {
			roomDao.update(room);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			roomDao.deleteById(id);
		}
	}

	private boolean isRoomValid(Room room) {
		return room.getName() != null
				&& isNameUnique(room)
				&& !room.getName().isEmpty()
				&& room.getCapacity() > 0;
	}

	private boolean isPresentById(Long id) {
		return roomDao.findById(id).isPresent();
	}

	private boolean isNameUnique(Room room) {
		Optional<Room> roomByName = roomDao.findByName(room.getName());
		if (roomByName.isPresent()) {
			return roomByName.get().getId().equals(room.getId());
		} else {
			return true;
		}
	}
}
