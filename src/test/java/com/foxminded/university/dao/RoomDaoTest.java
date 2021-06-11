package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.foxminded.university.model.Room;

@DataJpaTest
public class RoomDaoTest {

	@Autowired
	TestEntityManager entityManager;
	@Autowired
	RoomDao roomDao;

	@Test
	public void givenName_whenFindByName_thenGetRightRoom() {
		Optional<Room> expected = Optional.of(entityManager.find(Room.class, 1L));

		Optional<Room> actual = roomDao.findByName("A111");

		assertEquals(expected, actual);
	}
}
