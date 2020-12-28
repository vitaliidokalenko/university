package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.dao.jdbc.JdbcLessonDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

@SpringJUnitConfig(AppConfig.class)
public class JdbcLessonDaoTest {

	private static final String LESSONS_TABLE_NAME = "lessons";
	private static final String LESSONS_GROUPS_TABLE_NAME = "lessons_groups";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcLessonDao lessonDao;

	@Test
	@Sql({ "/schema.sql", "/dataLessons.sql" })
	public void givenLessons_whenGetAll_thenGetRightListOfLessons() {
		Timeframe timeframe1 = new Timeframe();
		timeframe1.setId(1L);
		timeframe1.setSequance(1);
		timeframe1.setStartTime(LocalTime.parse("08:00"));
		timeframe1.setEndTime(LocalTime.parse("09:20"));
		Timeframe timeframe2 = new Timeframe();
		timeframe2.setId(2L);
		timeframe2.setSequance(2);
		timeframe2.setStartTime(LocalTime.parse("09:40"));
		timeframe2.setEndTime(LocalTime.parse("11:00"));
		Timeframe timeframe3 = new Timeframe();
		timeframe3.setId(3L);
		timeframe3.setSequance(3);
		timeframe3.setStartTime(LocalTime.parse("11:20"));
		timeframe3.setEndTime(LocalTime.parse("12:40"));
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Teacher teacher1 = new Teacher("Victor", "Doncov");
		teacher1.setId(1L);
		teacher1.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher1.setGender(Gender.MALE);
		Teacher teacher2 = new Teacher("Aleksandra", "Ivanova");
		teacher2.setId(2L);
		teacher2.setBirthdate(LocalDate.parse("1992-02-02"));
		teacher2.setGender(Gender.FEMALE);
		Teacher teacher3 = new Teacher("Anatoly", "Sviridov");
		teacher3.setId(3L);
		teacher3.setBirthdate(LocalDate.parse("1993-03-03"));
		teacher3.setGender(Gender.MALE);
		Room room1 = new Room("A111");
		room1.setId(1L);
		room1.setCapacity(30);
		Room room2 = new Room("B222");
		room2.setId(2L);
		room2.setCapacity(30);
		Room room3 = new Room("C333");
		room3.setId(3L);
		room3.setCapacity(30);
		Lesson lesson1 = new Lesson();
		lesson1.setId(1L);
		lesson1.setDate(LocalDate.parse("2020-12-12"));
		lesson1.setTimeframe(timeframe1);
		lesson1.setCourse(course1);
		lesson1.setTeacher(teacher1);
		lesson1.setRoom(room1);
		Lesson lesson2 = new Lesson();
		lesson2.setId(2L);
		lesson2.setDate(LocalDate.parse("2020-12-12"));
		lesson2.setTimeframe(timeframe2);
		lesson2.setCourse(course2);
		lesson2.setTeacher(teacher2);
		lesson2.setRoom(room2);
		Lesson lesson3 = new Lesson();
		lesson3.setId(3L);
		lesson3.setDate(LocalDate.parse("2020-12-12"));
		lesson3.setTimeframe(timeframe3);
		lesson3.setCourse(course3);
		lesson3.setTeacher(teacher3);
		lesson3.setRoom(room3);
		List<Lesson> expected = Arrays.asList(lesson1, lesson2, lesson3);

		List<Lesson> actual = lessonDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataForLessons.sql" })
	public void givenLesson_whenCreate_thenLessonIsAddedToTable() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequance(1);
		timeframe.setStartTime(LocalTime.parse("08:00"));
		timeframe.setEndTime(LocalTime.parse("09:20"));
		Course course = new Course("Law");
		course.setId(1L);
		Teacher teacher = new Teacher("Victor", "Doncov");
		teacher.setId(1L);
		teacher.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher.setGender(Gender.MALE);
		Room room = new Room("A111");
		room.setId(1L);
		room.setCapacity(30);
		Lesson expected = new Lesson();
		expected.setId(1L);
		expected.setDate(LocalDate.parse("2020-12-12"));
		expected.setTimeframe(timeframe);
		expected.setCourse(course);
		expected.setTeacher(teacher);
		expected.setRoom(room);

		lessonDao.create(expected);

		Lesson actual = lessonDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataLessons.sql" })
	public void givenId_whenFindById_thenGetRightLesson() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequance(1);
		timeframe.setStartTime(LocalTime.parse("08:00"));
		timeframe.setEndTime(LocalTime.parse("09:20"));
		Course course = new Course("Law");
		course.setId(1L);
		Teacher teacher = new Teacher("Victor", "Doncov");
		teacher.setId(1L);
		teacher.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher.setGender(Gender.MALE);
		Room room = new Room("A111");
		room.setId(1L);
		room.setCapacity(30);
		Lesson expected = new Lesson();
		expected.setId(1L);
		expected.setDate(LocalDate.parse("2020-12-12"));
		expected.setTimeframe(timeframe);
		expected.setCourse(course);
		expected.setTeacher(teacher);
		expected.setRoom(room);

		Lesson actual = lessonDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataLessons.sql" })
	public void givenUpdatedFields_whenUpdate_thenGetRightLesson() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(2L);
		timeframe.setSequance(2);
		timeframe.setStartTime(LocalTime.parse("09:40"));
		timeframe.setEndTime(LocalTime.parse("11:00"));
		Course course = new Course("Music");
		course.setId(3L);
		Teacher teacher = new Teacher("Anatoly", "Sviridov");
		teacher.setId(3L);
		teacher.setBirthdate(LocalDate.parse("1993-03-03"));
		teacher.setGender(Gender.MALE);
		Room room = new Room("B222");
		room.setId(2L);
		room.setCapacity(30);
		Lesson expected = new Lesson();
		expected.setId(1L);
		expected.setDate(LocalDate.parse("2021-01-05"));
		expected.setTimeframe(timeframe);
		expected.setCourse(course);
		expected.setTeacher(teacher);
		expected.setRoom(room);

		lessonDao.update(expected);

		Lesson actual = lessonDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataLessons.sql" })
	public void givenLessonId_whenDeleteById_thenLessonIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, LESSONS_TABLE_NAME) - 1;

		lessonDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, LESSONS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/dataLessons.sql" })
	public void givenLessonIdAndGroupId_whenCreateLessonGroup_thenRightDataAddedToTable() {
		int expectedRows = countRowsInTable(jdbcTemplate, LESSONS_GROUPS_TABLE_NAME) + 3;

		lessonDao.createLessonsGroups(1L, 1L);
		lessonDao.createLessonsGroups(1L, 2L);
		lessonDao.createLessonsGroups(2L, 1L);

		int actualRows = countRowsInTable(jdbcTemplate, LESSONS_GROUPS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenLessonIdAndGroupId_whenDeleteLessonGroup_thenRightDataDeletedFromTable() {
		int expectedRows = countRowsInTable(jdbcTemplate, LESSONS_GROUPS_TABLE_NAME) - 1;

		lessonDao.deleteLessonsGroups(1L, 1L);

		int actualRows = countRowsInTable(jdbcTemplate, LESSONS_GROUPS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenGroupId_whenGetLessonsByGroupId_thenGetRightListOfLessons() {
		Timeframe timeframe1 = new Timeframe();
		timeframe1.setId(1L);
		timeframe1.setSequance(1);
		timeframe1.setStartTime(LocalTime.parse("08:00"));
		timeframe1.setEndTime(LocalTime.parse("09:20"));
		Timeframe timeframe2 = new Timeframe();
		timeframe2.setId(2L);
		timeframe2.setSequance(2);
		timeframe2.setStartTime(LocalTime.parse("09:40"));
		timeframe2.setEndTime(LocalTime.parse("11:00"));
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Teacher teacher1 = new Teacher("Victor", "Doncov");
		teacher1.setId(1L);
		teacher1.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher1.setGender(Gender.MALE);
		Teacher teacher2 = new Teacher("Aleksandra", "Ivanova");
		teacher2.setId(2L);
		teacher2.setBirthdate(LocalDate.parse("1992-02-02"));
		teacher2.setGender(Gender.FEMALE);
		Room room1 = new Room("A111");
		room1.setId(1L);
		room1.setCapacity(30);
		Room room2 = new Room("B222");
		room2.setId(2L);
		room2.setCapacity(30);
		Lesson lesson1 = new Lesson();
		lesson1.setId(1L);
		lesson1.setDate(LocalDate.parse("2020-12-12"));
		lesson1.setTimeframe(timeframe1);
		lesson1.setCourse(course1);
		lesson1.setTeacher(teacher1);
		lesson1.setRoom(room1);
		Lesson lesson2 = new Lesson();
		lesson2.setId(2L);
		lesson2.setDate(LocalDate.parse("2020-12-12"));
		lesson2.setTimeframe(timeframe2);
		lesson2.setCourse(course2);
		lesson2.setTeacher(teacher2);
		lesson2.setRoom(room2);
		List<Lesson> expected = Arrays.asList(lesson1, lesson2);

		List<Lesson> actual = lessonDao.getLessonsByGroupId(2L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenTimeframe_whenGetLessonsByTimeframe_thenGetRightListOfLessons() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequance(1);
		timeframe.setStartTime(LocalTime.parse("08:00"));
		timeframe.setEndTime(LocalTime.parse("09:20"));
		Course course = new Course("Law");
		course.setId(1L);
		Teacher teacher = new Teacher("Victor", "Doncov");
		teacher.setId(1L);
		teacher.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher.setGender(Gender.MALE);
		Room room = new Room("A111");
		room.setId(1L);
		room.setCapacity(30);
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		lesson.setDate(LocalDate.parse("2020-12-12"));
		lesson.setTimeframe(timeframe);
		lesson.setCourse(course);
		lesson.setTeacher(teacher);
		lesson.setRoom(room);
		List<Lesson> expected = Arrays.asList(lesson);

		List<Lesson> actual = lessonDao.getLessonsByTimeframe(timeframe);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenCourse_whenGetLessonsByCourse_thenGetRightListOfLessons() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequance(1);
		timeframe.setStartTime(LocalTime.parse("08:00"));
		timeframe.setEndTime(LocalTime.parse("09:20"));
		Course course = new Course("Law");
		course.setId(1L);
		Teacher teacher = new Teacher("Victor", "Doncov");
		teacher.setId(1L);
		teacher.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher.setGender(Gender.MALE);
		Room room = new Room("A111");
		room.setId(1L);
		room.setCapacity(30);
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		lesson.setDate(LocalDate.parse("2020-12-12"));
		lesson.setTimeframe(timeframe);
		lesson.setCourse(course);
		lesson.setTeacher(teacher);
		lesson.setRoom(room);
		List<Lesson> expected = Arrays.asList(lesson);

		List<Lesson> actual = lessonDao.getLessonsByCourse(course);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenTeacher_whenGetLessonsByTeacher_thenGetRightListOfLessons() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequance(1);
		timeframe.setStartTime(LocalTime.parse("08:00"));
		timeframe.setEndTime(LocalTime.parse("09:20"));
		Course course = new Course("Law");
		course.setId(1L);
		Teacher teacher = new Teacher("Victor", "Doncov");
		teacher.setId(1L);
		teacher.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher.setGender(Gender.MALE);
		Room room = new Room("A111");
		room.setId(1L);
		room.setCapacity(30);
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		lesson.setDate(LocalDate.parse("2020-12-12"));
		lesson.setTimeframe(timeframe);
		lesson.setCourse(course);
		lesson.setTeacher(teacher);
		lesson.setRoom(room);
		List<Lesson> expected = Arrays.asList(lesson);

		List<Lesson> actual = lessonDao.getLessonsByTeacher(teacher);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenRoom_whenGetLessonsByRoom_thenGetRightListOfLessons() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequance(1);
		timeframe.setStartTime(LocalTime.parse("08:00"));
		timeframe.setEndTime(LocalTime.parse("09:20"));
		Course course = new Course("Law");
		course.setId(1L);
		Teacher teacher = new Teacher("Victor", "Doncov");
		teacher.setId(1L);
		teacher.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher.setGender(Gender.MALE);
		Room room = new Room("A111");
		room.setId(1L);
		room.setCapacity(30);
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		lesson.setDate(LocalDate.parse("2020-12-12"));
		lesson.setTimeframe(timeframe);
		lesson.setCourse(course);
		lesson.setTeacher(teacher);
		lesson.setRoom(room);
		List<Lesson> expected = Arrays.asList(lesson);

		List<Lesson> actual = lessonDao.getLessonsByRoom(room);

		assertEquals(expected, actual);
	}
}
