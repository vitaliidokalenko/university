package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
	public void givenGroupIdAndDateAndTimeframe_whenGetByGroupIdAndDateAndTimeframe_thenGetRightLesson() {
		LocalDate date = LocalDate.parse("2020-12-12");
		Timeframe timeframe = entityManager.find(Timeframe.class, 1L);
		Optional<Lesson> expected = Optional.of(entityManager.find(Lesson.class, 1L));

		Optional<Lesson> actual = lessonDao.getByGroupIdAndDateAndTimeframe(1L, date, timeframe);

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
	public void givenTeacherIdAndDates_whenGetByTeacherIdAndDateBetween_thenGetRightListOfLessons() {
		LocalDate startDate = LocalDate.parse("2020-12-11");
		LocalDate endDate = LocalDate.parse("2020-12-15");
		List<Lesson> expected = List.of(entityManager.find(Lesson.class, 1L));

		List<Lesson> actual = lessonDao.getByTeacherIdAndDateBetween(1L, startDate, endDate);

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroupIdAndDates_whenGetByGroupIdAndDateBetween_thenGetRightListOfLessons() {
		LocalDate startDate = LocalDate.parse("2020-12-11");
		LocalDate endDate = LocalDate.parse("2020-12-15");
		List<Lesson> expected = List.of(entityManager.find(Lesson.class, 1L), entityManager.find(Lesson.class, 2L));

		List<Lesson> actual = lessonDao.getByGroupIdAndDateBetween(1L, startDate, endDate);

		assertEquals(expected, actual);
	}
}
