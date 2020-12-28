package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.dao.jdbc.JdbcRoomDao;
import com.foxminded.university.model.Room;

@SpringJUnitConfig(AppConfig.class)
public class JdbcRoomDaoTest {

	private static final String ROOMS_TABLE_NAME = "rooms";

	private JdbcTemplate jdbcTemplate;
	private JdbcRoomDao roomDao;

	@Autowired
	public JdbcRoomDaoTest(DataSource dataSource, JdbcRoomDao roomDao) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.roomDao = roomDao;
	}

	@Test
	@Sql({ "/schema.sql", "/dataRooms.sql" })
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
	@Sql("/schema.sql")
	public void givenRoom_whenCreate_thenRoomIsAddedToTable() {
		Room expected = new Room("A111");
		expected.setCapacity(30);

		roomDao.create(expected);

		Room actual = roomDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataRooms.sql" })
	public void givenId_whenFindById_thenGetRightRoom() {
		Room expected = new Room("A111");
		expected.setId(1L);
		expected.setCapacity(30);

		Room actual = roomDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataRooms.sql" })
	public void givenUpdatedFields_whenUpdate_thenGetRightRoom() {
		Room expected = new Room("D444");
		expected.setId(1L);
		expected.setCapacity(30);

		roomDao.update(expected);

		Room actual = roomDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataRooms.sql" })
	public void givenRoomId_whenDeleteById_thenRoomIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, ROOMS_TABLE_NAME) - 1;

		roomDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, ROOMS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
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
