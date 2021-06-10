package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

@DataJpaTest
public class LessonDaoTest {

	@Autowired
	TestEntityManager entityManager;
	@Autowired
	LessonDao lessonDao;

	@Test
	public void givenGroupAndDateAndTimeframe_whenGetByGroupAndDateAndTimeframe_thenGetRightLesson() {
		LocalDate date = LocalDate.parse("2020-12-12");
		Timeframe timeframe = entityManager.find(Timeframe.class, 1L);
		Group group = entityManager.find(Group.class, 1L);
		Optional<Lesson> expected = Optional.of(entityManager.find(Lesson.class, 1L));

		Optional<Lesson> actual = lessonDao.getByGroupsAndDateAndTimeframe(group, date, timeframe);

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacherAndDateAndTimeframe_whenGetByTeacherAndDateAndTimeframe_thenGetRightLesson() {
		Teacher teacher = entityManager.find(Teacher.class, 1L);
		LocalDate date = LocalDate.parse("2020-12-12");
		Timeframe timeframe = entityManager.find(Timeframe.class, 1L);
		Optional<Lesson> expected = Optional.of(entityManager.find(Lesson.class, 1L));

		Optional<Lesson> actual = lessonDao.getByTeacherAndDateAndTimeframe(teacher, date, timeframe);

		assertEquals(expected, actual);
	}

	@Test
	public void givenRoomAndDateAndTimeframe_whenGetByRoomAndDateAndTimeframe_thenGetRightLesson() {
		Room room = entityManager.find(Room.class, 1L);
		LocalDate date = LocalDate.parse("2020-12-12");
		Timeframe timeframe = entityManager.find(Timeframe.class, 1L);
		Optional<Lesson> expected = Optional.of(entityManager.find(Lesson.class, 1L));

		Optional<Lesson> actual = lessonDao.getByRoomAndDateAndTimeframe(room, date, timeframe);

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacherAndDates_whenGetByTeacherAndDateBetween_thenGetRightListOfLessons() {
		LocalDate startDate = LocalDate.parse("2020-12-11");
		LocalDate endDate = LocalDate.parse("2020-12-15");
		Lesson lesson = entityManager.find(Lesson.class, 1L);
		List<Lesson> expected = List.of(lesson);

		List<Lesson> actual = lessonDao.getByTeacherAndDateBetween(lesson.getTeacher(), startDate, endDate);

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroupAndDates_whenGetByGroupsAndDateBetween_thenGetRightListOfLessons() {
		LocalDate startDate = LocalDate.parse("2020-12-11");
		LocalDate endDate = LocalDate.parse("2020-12-15");
		Group group = entityManager.find(Group.class, 1L);
		List<Lesson> expected = List.of(entityManager.find(Lesson.class, 1L), entityManager.find(Lesson.class, 2L));

		List<Lesson> actual = lessonDao.getByGroupsAndDateBetween(group, startDate, endDate);

		assertEquals(expected, actual);
	}
}
