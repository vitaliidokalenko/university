package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;

@SpringJUnitConfig(AppConfig.class)
public class JdbcCourseDaoTest {

	private static final String COURSES_ROOMS_TABLE_NAME = "courses_rooms";
	private static final String COURSES_TABLE_NAME = "courses";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcCourseDao courseDao;

	@Test
	@Sql({ "/schema.sql", "/dataCourses.sql" })
	public void givenCourses_whenGetAll_thenGetRightListOfCourses() {
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		List<Course> expected = Arrays.asList(course1, course2, course3);

		List<Course> actual = courseDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/schema.sql")
	public void givenCourse_whenCreate_thenCourseIsAddedToTable() {
		Course course = new Course("Law");
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_TABLE_NAME) + 1;

		courseDao.create(course);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/dataRooms.sql" })
	public void givenCourseWithRooms_whenCreate_thenRightDataAddedToCoursesRoomsTable() {
		Course course = new Course("Law");
		Room room1 = new Room("A111");
		room1.setId(1L);
		Room room2 = new Room("B222");
		room2.setId(2L);
		Set<Room> rooms = new HashSet<>();
		rooms.add(room1);
		rooms.add(room2);
		course.setRooms(rooms);
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME) + 2;

		courseDao.create(course);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/dataCourses.sql" })
	public void givenId_whenFindById_thenGetRightCourse() {
		Course expected = new Course("Law");
		expected.setId(1L);

		Course actual = courseDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataCourses.sql" })
	public void givenUpdatedFields_whenUpdate_thenCourseTableIsUpdated() {
		Course course = new Course("Art");
		course.setId(1L);
		int expectedRows = countRowsInTableWhere(jdbcTemplate, COURSES_TABLE_NAME, "name = 'Art'") + 1;

		courseDao.update(course);

		int actualRows = countRowsInTableWhere(jdbcTemplate, COURSES_TABLE_NAME, "name = 'Art'");
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/dataCoursesRooms.sql" })
	public void givenUpdatedRooms_whenUpdate_thenCoursesRoomsTableIsUpdated() {
		Course course = new Course("Art");
		course.setId(1L);
		Room room = new Room("D444");
		room.setId(4L);
		Set<Room> rooms = new HashSet<>();
		rooms.add(room);
		course.setRooms(rooms);
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME) - 2;

		courseDao.update(course);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/dataCourses.sql" })
	public void givenCourseId_whenDeleteById_thenCourseIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_TABLE_NAME) - 1;

		courseDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenRoomId_whenGetCoursesByRoomId_thenGetRightListOfCourses() {
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		List<Course> expected = Arrays.asList(course1, course2);

		List<Course> actual = courseDao.getCoursesByRoomId(2L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenStudentId_whenGetCoursesByStudentId_thenGetRightListOfCourses() {
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		List<Course> expected = Arrays.asList(course1, course2);

		List<Course> actual = courseDao.getCoursesByStudentId(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenTeacherId_whenGetCoursesByTeacherId_thenGetRightListOfCourses() {
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		List<Course> expected = Arrays.asList(course1, course2);

		List<Course> actual = courseDao.getCoursesByTeacherId(1L);

		assertEquals(expected, actual);
	}
}
