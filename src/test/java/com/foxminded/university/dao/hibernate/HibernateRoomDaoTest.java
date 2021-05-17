package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.model.Room;

@SpringJUnitConfig(TestAppConfig.class)
@Transactional
public class HibernateRoomDaoTest {

	@Autowired
	HibernateTemplate template;
	@Autowired
	HibernateRoomDao roomDao;

	@Test
	public void whenGetAll_thenGetRightListOfRooms() {
		List<Room> expected = template.loadAll(Room.class);

		List<Room> actual = roomDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenNewRoom_whenCreate_thenCreated() {
		Room expected = Room.builder().name("E555").build();

		roomDao.create(expected);

		Room actual = template.get(Room.class, expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenFindById_thenGetRightRoom() {
		Optional<Room> expected = Optional.of(template.get(Room.class, 1L));

		Optional<Room> actual = roomDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenWrongId_whenFindById_thenGetEmptyOptional() {
		Optional<Room> expected = Optional.empty();

		Optional<Room> actual = roomDao.findById(10L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenUpdatedFields_whenUpdate_thenRoomUpdated() {
		String expectedName = "Updated Name";
		Room room = template.get(Room.class, 1L);
		room.setName(expectedName);

		roomDao.update(room);

		assertEquals(expectedName, template.get(Room.class, 1L).getName());
	}

	@Test
	public void givenRoom_whenDelete_thenDeleted() {
		Room room = template.get(Room.class, 4L);

		roomDao.delete(room);

		assertNull(template.get(Room.class, 4L));
	}

	@Test
	public void whenCount_thenGetRightAmountOfRoom() {
		long expected = template.loadAll(Room.class).size();

		long actual = roomDao.count();

		assertEquals(expected, actual);
	}

	@Test
	public void givenPageSize_whenGetAllPage_thenGetRightRooms() {
		List<Room> expected = template.loadAll(Room.class).subList(0, 2);
		int pageSize = 2;

		Page<Room> actual = roomDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}

	@Test
	public void givenName_whenFindByName_thenGetRightRoom() {
		Optional<Room> expected = Optional.of(template.get(Room.class, 1L));

		Optional<Room> actual = roomDao.findByName("A111");

		assertEquals(expected, actual);
	}

	@Test
	public void givenCourseId_whenGetByCourseId_thenGetRightListOfRooms() {
		List<Room> expected = List.of(template.get(Room.class, 1L), template.get(Room.class, 2L));

		List<Room> actual = roomDao.getByCourseId(1L);

		assertEquals(expected, actual);
	}
}
