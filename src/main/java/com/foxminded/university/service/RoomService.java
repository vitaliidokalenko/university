package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;
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
		roomDao.save(room);
	}

	@Transactional
	public Optional<Room> findById(Long id) {
		logger.debug("Finding room by id: {}", id);
		return roomDao.findById(id);
	}

	@Transactional
	public List<Room> getAll() {
		logger.debug("Getting rooms");
		return roomDao.findAll();
	}

	@Transactional
	public Page<Room> getAllPage(Pageable pageable) {
		logger.debug("Getting pageable rooms");
		return roomDao.findAll(pageable);
	}

	@Transactional
	public void update(Room room) {
		logger.debug("Updating room: {}", room);
		verifyExistence(room);
		verify(room);
		roomDao.save(room);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting room by id: {}", id);
		roomDao.delete(roomDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find room by id: %d", id))));
	}

	private void verify(Room room) {
		verifyNameIsUnique(room);
	}

	private void verifyNameIsUnique(Room room) {
		Optional<Room> roomByName = roomDao.findByName(room.getName());
		if (roomByName.isPresent() && !roomByName.get().getId().equals(room.getId())) {
			throw new NotUniqueNameException(format("The room with name %s already exists", room.getName()));
		}
	}

	private void verifyExistence(Room room) {
		if (!roomDao.existsById(room.getId())) {
			throw new NotFoundEntityException(format("Cannot find room by id: %d", room.getId()));
		}
	}
}
