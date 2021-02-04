package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.exception.DAOException;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.exception.ServiceException;

@Service
public class RoomService {

	private RoomDao roomDao;

	public RoomService(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	@Transactional
	public void create(Room room) {
		if (isRoomValid(room)) {
			try {
				roomDao.create(room);
			} catch (DAOException e) {
				throw new ServiceException("Could not create room: " + room, e);
			}
		}
	}

	@Transactional
	public Optional<Room> findById(Long id) {
		try {
			return roomDao.findById(id);
		} catch (DAOException e) {
			throw new ServiceException("Could not get room by id: " + id, e);
		}
	}

	@Transactional
	public List<Room> getAll() {
		try {
			return roomDao.getAll();
		} catch (DAOException e) {
			throw new ServiceException("Could not get rooms", e);
		}
	}

	@Transactional
	public void update(Room room) {
		if (isRoomValid(room)) {
			try {
				roomDao.update(room);
			} catch (DAOException e) {
				throw new ServiceException("Cold not update room: " + room, e);
			}
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			try {
				roomDao.deleteById(id);
			} catch (DAOException e) {
				throw new ServiceException("Could not delete room by id: " + id, e);
			}
		}
	}

	private boolean isRoomValid(Room room) {
		return room.getName() != null
				&& !room.getName().isEmpty()
				&& isNameUnique(room)
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
