package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.dao.jdbc.JdbcCourseDao;
import com.foxminded.university.model.Course;

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
		Course expected = new Course("Law");

		courseDao.create(expected);

		Course actual = courseDao.getAll().get(0);
		assertEquals(expected, actual);
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
	public void givenUpdatedFields_whenUpdate_thenGetRightCourse() {
		Course expected = new Course("Art");
		expected.setId(1L);

		courseDao.update(expected);

		Course actual = courseDao.getAll().get(0);
		assertEquals(expected, actual);
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
	@Sql({ "/schema.sql", "/dataCoursesRooms.sql" })
	public void givenCourseIdAndRoomId_whenCreateCourseRoom_thenRightDataAddedToTable() {
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME) + 2;

		courseDao.createCourseRoom(1L, 1L);
		courseDao.createCourseRoom(1L, 2L);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenCourseIdAndRoomId_whenDeleteCourseRoom_thenRightDataDeletedFromTable() {
		int expectedRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME) - 1;

		courseDao.deleteCourseRoom(1L, 1L);

		int actualRows = countRowsInTable(jdbcTemplate, COURSES_ROOMS_TABLE_NAME);
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
