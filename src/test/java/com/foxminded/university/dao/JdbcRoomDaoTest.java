package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.jdbc.JdbcRoomDao;
import com.foxminded.university.model.Room;

@SpringJUnitConfig(TestAppConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcRoomDaoTest {

	private static final String ROOMS_TABLE_NAME = "rooms";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcRoomDao roomDao;

	@Test
	@Sql("/dataRooms.sql")
	public void givenRooms_whenGetAll_thenGetRightListOfRooms() {
		Room room1 = new Room("A111");
		room1.setId(1L);
		room1.setCapacity(30);
		Room room2 = new Room("B222");
		room2.setId(2L);
		room2.setCapacity(30);
		Room room3 = new Room("C333");
		room3.setId(3L);
		room3.setCapacity(30);
		List<Room> expected = Arrays.asList(room1, room2, room3);

		List<Room> actual = roomDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenRoom_whenCreate_thenRoomIsAddedToTable() {
		Room room = new Room("A111");
		room.setCapacity(30);
		int expectedRows = countRowsInTable(jdbcTemplate, ROOMS_TABLE_NAME) + 1;

		roomDao.create(room);

		int actualRows = countRowsInTable(jdbcTemplate, ROOMS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataRooms.sql")
	public void givenId_whenFindById_thenGetRightRoom() {
		Room expected = new Room("A111");
		expected.setId(1L);
		expected.setCapacity(30);

		Room actual = roomDao.findById(1L).orElse(null);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataRooms.sql")
	public void givenUpdatedFields_whenUpdate_thenRoomsTableIsUpdated() {
		Room room = new Room("D444");
		room.setId(1L);
		room.setCapacity(30);
		int expectedRows = countRowsInTableWhere(jdbcTemplate, ROOMS_TABLE_NAME, "name = 'D444'") + 1;

		roomDao.update(room);

		int actualRows = countRowsInTableWhere(jdbcTemplate, ROOMS_TABLE_NAME, "name = 'D444'");
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataRooms.sql")
	public void givenRoomId_whenDeleteById_thenRoomIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, ROOMS_TABLE_NAME) - 1;

		roomDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, ROOMS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/data.sql")
	public void givenCourseId_whenGetRoomsByCourseId_thenGetRightListOfRooms() {
		Room room1 = new Room("A111");
		room1.setId(1L);
		room1.setCapacity(30);
		Room room2 = new Room("B222");
		room2.setId(2L);
		room2.setCapacity(30);
		List<Room> expected = Arrays.asList(room1, room2);

		List<Room> actual = roomDao.getRoomsByCourseId(1L);

		assertEquals(expected, actual);
	}
}
