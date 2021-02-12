package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

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
		if (roomDao.findById(id).isPresent()) {
			roomDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(format("Cannot find room by id: %d", id));
		}
	}

	private void verify(Room room) {
		verifyFields(room);
		verifyNameIsUnique(room);
	}

	private void verifyFields(Room room) {
		if (StringUtils.isEmpty(room.getName())) {
			throw new IllegalFieldEntityException("Empty room name");
		} else if (room.getCapacity() < 1) {
			throw new IllegalFieldEntityException(format("Capacity of the room %s is less than 1", room.getName()));
		}
	}

	private void verifyNameIsUnique(Room room) {
		Optional<Room> roomByName = roomDao.findByName(room.getName());
		if (roomByName.isPresent() && !roomByName.get().getId().equals(room.getId())) {
			throw new NotUniqueNameException(format("The room with name %s already exists", room.getName()));
		}
	}
}
