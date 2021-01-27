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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;

@SpringJUnitConfig(TestAppConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcCourseDaoTest {

	private static final String COURSES_ROOMS_TABLE_NAME = "courses_rooms";
	private static final String COURSES_TABLE_NAME = "courses";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcCourseDao courseDao;

	@Test
	@Sql("/dataCourses.sql")
	public void givenCourses_whenGetAll_thenGetRightListOfCourses() {
		Room room1 = new Room("A111");
		room1.setId(1L);
		room1.setCapacity(30);
		Room room2 = new Room("B222");
		room2.setId(2L);
		room2.setCapacity(30);
		Set<Room> rooms = new HashSet<>(Arrays.asList(room1, room2));
		Course course1 = new Course("Law");
		course1.setId(1L);
		course1.setRooms(rooms);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		course2.setRooms(rooms);
		Course course3 = new Course("Music");
		course3.setId(3L);
		course3.setRooms(rooms);
		List<Course> expected = Arrays.asList(course1, course2, course3);

		List<Course> actual = courseDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenCourse_whenCreate_thenCourseIsAddedToTable() {
		Course course = new Course("Law");
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_TABLE_NAME) + 1;

		courseDao.create(course);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataRooms.sql")
	public void givenCourseWithRooms_whenCreate_thenRightDataAddedToCoursesRoomsTable() {
		Course course = new Course("Law");
		Room room1 = new Room("A111");
		room1.setId(1L);
		Room room2 = new Room("B222");
		room2.setId(2L);
		course.setRooms(new HashSet<>(Arrays.asList(room1, room2)));
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME) + 2;

		courseDao.create(course);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataCourses.sql")
	public void givenId_whenFindById_thenGetRightCourse() {
		Room room1 = new Room("A111");
		room1.setId(1L);
		room1.setCapacity(30);
		Room room2 = new Room("B222");
		room2.setId(2L);
		room2.setCapacity(30);
		Course expected = new Course("Law");
		expected.setId(1L);
		expected.setRooms(new HashSet<>(Arrays.asList(room1, room2)));

		Course actual = courseDao.findById(1L).orElse(null);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataCourses.sql")
	public void givenUpdatedFields_whenUpdate_thenCourseTableIsUpdated() {
		Course course = new Course("Art");
		course.setId(1L);
		int expectedRows = countRowsInTableWhere(jdbcTemplate, COURSES_TABLE_NAME, "name = 'Art'") + 1;

		courseDao.update(course);

		int actualRows = countRowsInTableWhere(jdbcTemplate, COURSES_TABLE_NAME, "name = 'Art'");
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataCourses.sql")
	public void givenUpdatedRooms_whenUpdate_thenCoursesRoomsTableIsUpdated() {
		Course course = new Course("Art");
		course.setId(1L);
		Room room = new Room("D444");
		room.setId(4L);
		course.setRooms(new HashSet<>(Arrays.asList(room)));
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME) - 1;

		courseDao.update(course);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataCourses.sql")
	public void givenCourseId_whenDeleteById_thenCourseIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_TABLE_NAME) - 1;

		courseDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/data.sql")
	public void givenRoomId_whenGetCoursesByRoomId_thenGetRightListOfCourses() {
		Room room1 = new Room("A111");
		room1.setId(1L);
		room1.setCapacity(30);
		Room room2 = new Room("B222");
		room2.setId(2L);
		room2.setCapacity(30);
		Room room3 = new Room("C333");
		room3.setId(3L);
		room3.setCapacity(30);
		Course course1 = new Course("Law");
		course1.setId(1L);
		course1.setRooms(new HashSet<>(Arrays.asList(room1, room2)));
		Course course2 = new Course("Biology");
		course2.setId(2L);
		course2.setRooms(new HashSet<>(Arrays.asList(room2, room3)));
		List<Course> expected = Arrays.asList(course1, course2);

		List<Course> actual = courseDao.getCoursesByRoomId(2L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/data.sql")
	public void givenStudentId_whenGetCoursesByStudentId_thenGetRightListOfCourses() {
		Room room1 = new Room("A111");
		room1.setId(1L);
		room1.setCapacity(30);
		Room room2 = new Room("B222");
		room2.setId(2L);
		room2.setCapacity(30);
		Room room3 = new Room("C333");
		room3.setId(3L);
		room3.setCapacity(30);
		Course course1 = new Course("Law");
		course1.setId(1L);
		course1.setRooms(new HashSet<>(Arrays.asList(room1, room2)));
		Course course2 = new Course("Biology");
		course2.setId(2L);
		course2.setRooms(new HashSet<>(Arrays.asList(room2, room3)));
		List<Course> expected = Arrays.asList(course1, course2);

		List<Course> actual = courseDao.getCoursesByStudentId(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/data.sql")
	public void givenTeacherId_whenGetCoursesByTeacherId_thenGetRightListOfCourses() {
		Room room1 = new Room("A111");
		room1.setId(1L);
		room1.setCapacity(30);
		Room room2 = new Room("B222");
		room2.setId(2L);
		room2.setCapacity(30);
		Room room3 = new Room("C333");
		room3.setId(3L);
		room3.setCapacity(30);
		Course course1 = new Course("Law");
		course1.setId(1L);
		course1.setRooms(new HashSet<>(Arrays.asList(room1, room2)));
		Course course2 = new Course("Biology");
		course2.setId(2L);
		course2.setRooms(new HashSet<>(Arrays.asList(room2, room3)));
		List<Course> expected = Arrays.asList(course1, course2);

		List<Course> actual = courseDao.getCoursesByTeacherId(1L);

		assertEquals(expected, actual);
	}
}
