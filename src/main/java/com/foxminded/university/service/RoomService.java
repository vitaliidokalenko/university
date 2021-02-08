package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.exception.AlreadyExistsEntityException;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Service
public class RoomService {

	private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

	private RoomDao roomDao;

	public RoomService(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	@Transactional
	public void create(Room room) {
		logger.debug("Creating room: {}", room);
		verify(room);
		roomDao.create(room);
	}

	@Transactional
	public Optional<Room> findById(Long id) {
		logger.debug("Finding room by id: {}", id);
		return roomDao.findById(id);
	}

	@Transactional
	public List<Room> getAll() {
		logger.debug("Getting rooms");
		return roomDao.getAll();
	}

	@Transactional
	public void update(Room room) {
		logger.debug("Updating room: {}", room);
		verify(room);
		roomDao.update(room);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting room by id: {}", id);
		if (isPresentById(id)) {
			roomDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(format("There is nothing to delete. Room with id: %d is absent", id));
		}
	}

	private void verify(Room room) {
		if (room.getName() == null) {
			throw new IllegalFieldEntityException("The name of the room is absent");
		} else if (room.getName().isEmpty()) {
			throw new IllegalFieldEntityException("The name of the room is empty");
		} else if (!isNameUnique(room)) {
			throw new AlreadyExistsEntityException(format("The room with name %s already exists", room.getName()));
		} else if (room.getCapacity() < 1) {
			throw new IllegalFieldEntityException(format("Capacity of the room %s is less than 1", room.getName()));
		}
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
