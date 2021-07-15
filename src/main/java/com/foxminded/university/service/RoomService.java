package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoomService {

	private RoomDao roomDao;

	public RoomService(RoomDao roomDao) {
		this.roomDao = roomDao;
	}

	@Transactional
	public void create(Room room) {
		log.debug("Creating room: {}", room);
		verify(room);
		roomDao.save(room);
	}

	@Transactional
	public Room findById(Long id) {
		log.debug("Finding room by id: {}", id);
		return roomDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find room by id: %d", id)));
	}

	@Transactional
	public List<Room> getAll() {
		log.debug("Getting rooms");
		return roomDao.findAll();
	}

	@Transactional
	public Page<Room> getAllPage(Pageable pageable) {
		log.debug("Getting pageable rooms");
		return roomDao.findAll(pageable);
	}

	@Transactional
	public void update(Room room) {
		log.debug("Updating room: {}", room);
		if (findById(room.getId()) != null) {
			verify(room);
			roomDao.save(room);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		log.debug("Deleting room by id: {}", id);
		roomDao.delete(findById(id));
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
}
