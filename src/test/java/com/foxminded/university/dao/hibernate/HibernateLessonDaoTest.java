package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

@SpringJUnitConfig(TestAppConfig.class)
@Transactional
public class HibernateLessonDaoTest {

	@Autowired
	HibernateTemplate template;
	@Autowired
	HibernateLessonDao lessonDao;

	@Test
	public void givenNewCourse_whenCreate_thenCreated() {
		Lesson expected = Lesson.builder()
				.date(LocalDate.parse("2021-01-21"))
				.groups(Set.of(template.get(Group.class, 2L)))
				.teacher(template.get(Teacher.class, 2L))
				.course(template.get(Course.class, 2L))
				.room(template.get(Room.class, 2L))
				.timeframe(template.get(Timeframe.class, 2L))
				.build();

		lessonDao.create(expected);

		Lesson actual = template.get(Lesson.class, 4L);
		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenFindById_thenGetRightLesson() {
		Optional<Lesson> expected = Optional.of(template.get(Lesson.class, 1L));

		Optional<Lesson> actual = lessonDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenWrongId_whenFindById_thenGetEmptyOptional() {
		Optional<Lesson> expected = Optional.empty();

		Optional<Lesson> actual = lessonDao.findById(10L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenUpdatedFields_whenUpdate_thenLessonUpdated() {
		LocalDate expectedDate = LocalDate.parse("2022-01-01");
		Lesson lesson = template.get(Lesson.class, 1L);
		lesson.setDate(expectedDate);

		lessonDao.update(lesson);

		assertEquals(expectedDate, template.get(Lesson.class, 1L).getDate());
	}

	@Test
	public void givenUpdatedGroups_whenUpdate_thenLessonsGroupsUpdated() {
		Group expectedGroup = template.get(Group.class, 3L);
		Lesson lesson = template.get(Lesson.class, 1L);
		lesson.getGroups().add(expectedGroup);

		lessonDao.update(lesson);

		assertTrue(template.get(Lesson.class, 1L).getGroups().contains(expectedGroup));
	}

	@Test
	public void givenLesson_whenDelete_thenDeleted() {
		Lesson lesson = template.get(Lesson.class, 1L);
		int expectedRows = template.loadAll(Lesson.class).size() - 1;

		lessonDao.delete(lesson);

		int actualRows = template.loadAll(Lesson.class).size();
		assertEquals(expectedRows, actualRows);
	}

	@Test
	public void whenCount_thenGetRightAmountOfLessons() {
		long expected = template.loadAll(Lesson.class).size();

		long actual = lessonDao.count();

		assertEquals(expected, actual);
	}

	@Test
	public void givenPageSize_whenGetAllPage_thenGetRightLessons() {
		List<Lesson> expected = template.loadAll(Lesson.class).subList(0, 2);
		int pageSize = 2;

		Page<Lesson> actual = lessonDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}

	@Test
	public void givenTimeframe_whenGetByTimeframe_thenGetRightListOfLessons() {
		Timeframe timeframe = template.get(Timeframe.class, 1L);
		List<Lesson> expected = List.of(template.get(Lesson.class, 1L));

		List<Lesson> actual = lessonDao.getByTimeframe(timeframe);

		assertEquals(expected, actual);
	}

	@Test
	public void givenCourse_whenGetByCourse_thenGetRightListOfLessons() {
		Course course = template.get(Course.class, 1L);
		List<Lesson> expected = List.of(template.get(Lesson.class, 1L));

		List<Lesson> actual = lessonDao.getByCourse(course);

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroupIdAndDateAndTimeframe_whenGetByGroupIdAndDateAndTimeframe_thenGetRightLesson() {
		LocalDate date = LocalDate.parse("2020-12-12");
		Timeframe timeframe = template.get(Timeframe.class, 1L);
		Optional<Lesson> expected = Optional.of(template.get(Lesson.class, 1L));

		Optional<Lesson> actual = lessonDao.getByGroupIdAndDateAndTimeframe(1L, date, timeframe);

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacherAndDateAndTimeframe_whenGetByTeacherAndDateAndTimeframe_thenGetRightLesson() {
		Teacher teacher = template.get(Teacher.class, 1L);
		LocalDate date = LocalDate.parse("2020-12-12");
		Timeframe timeframe = template.get(Timeframe.class, 1L);
		Optional<Lesson> expected = Optional.of(template.get(Lesson.class, 1L));

		Optional<Lesson> actual = lessonDao.getByTeacherAndDateAndTimeframe(teacher, date, timeframe);

		assertEquals(expected, actual);
	}

	@Test
	public void givenRoomAndDateAndTimeframe_whenGetByRoomAndDateAndTimeframe_thenGetRightLesson() {
		Room room = template.get(Room.class, 1L);
		LocalDate date = LocalDate.parse("2020-12-12");
		Timeframe timeframe = template.get(Timeframe.class, 1L);
		Optional<Lesson> expected = Optional.of(template.get(Lesson.class, 1L));

		Optional<Lesson> actual = lessonDao.getByRoomAndDateAndTimeframe(room, date, timeframe);

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacherIdAndDates_whenGetByTeacherIdAndDateBetween_thenGetRightListOfLessons() {
		LocalDate startDate = LocalDate.parse("2020-12-11");
		LocalDate endDate = LocalDate.parse("2020-12-15");
		List<Lesson> expected = List.of(template.get(Lesson.class, 1L));

		List<Lesson> actual = lessonDao.getByTeacherIdAndDateBetween(1L, startDate, endDate);

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroupIdAndDates_whenGetByGroupIdAndDateBetween_thenGetRightListOfLessons() {
		LocalDate startDate = LocalDate.parse("2020-12-11");
		LocalDate endDate = LocalDate.parse("2020-12-15");
		List<Lesson> expected = List.of(template.get(Lesson.class, 1L), template.get(Lesson.class, 2L));

		List<Lesson> actual = lessonDao.getByGroupIdAndDateBetween(1L, startDate, endDate);

		assertEquals(expected, actual);
	}
}
