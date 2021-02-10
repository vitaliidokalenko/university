package com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.exception.IncompatibleDateException;
import com.foxminded.university.service.exception.IncompatibleRoomException;
import com.foxminded.university.service.exception.LackOfCapacityException;
import com.foxminded.university.service.exception.NotAvailableGroupException;
import com.foxminded.university.service.exception.NotAvailableRoomException;
import com.foxminded.university.service.exception.NotAvailableTeacherException;
import com.foxminded.university.service.exception.NotCompetentTeacherException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

	@Mock
	private LessonDao lessonDao;
	@Mock
	private StudentDao studentDao;

	@InjectMocks
	private LessonService lessonService;

	@Test
	public void givenLesson_whenCreate_thenLessonIsCreating() {
		Lesson lesson = buildLesson();

		lessonService.create(lesson);

		verify(lessonDao).create(lesson);
	}

	@Test
	public void givenTeacherIsNotAvailable_whenCreate_thenThrowException() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(2L);
		Lesson actual = buildLesson();
		when(lessonDao.getByTeacherAndDateAndTimeframe(actual.getTeacher(), actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.of(lessonByCriteria));

		Exception exception = assertThrows(NotAvailableTeacherException.class,
				() -> lessonService.create(actual));
		assertEquals(format("Teacher %s %s is busy at the time %s, %s",
				actual.getTeacher().getName(),
				actual.getTeacher().getSurname(),
				actual.getTimeframe().getStartTime().toString(),
				actual.getDate().toString()),
				exception.getMessage());
	}

	@Test
	public void givenTeacherIsAvailable_whenCreate_thenLessonIsCreating() {
		Lesson actual = buildLesson();
		when(lessonDao.getByTeacherAndDateAndTimeframe(actual.getTeacher(), actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.empty());

		lessonService.create(actual);

		verify(lessonDao).create(actual);
	}

	@Test
	public void givenRoomIsNotAvailable_whenCreate_thenThrowExceptin() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(2L);
		Lesson actual = buildLesson();
		when(lessonDao.getByRoomAndDateAndTimeframe(actual.getRoom(), actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.of(lessonByCriteria));

		Exception exception = assertThrows(NotAvailableRoomException.class,
				() -> lessonService.create(actual));
		assertEquals(format("Room %s is occupied at the time %s, %s",
				actual.getRoom().getName(),
				actual.getTimeframe().getStartTime().toString(),
				actual.getDate().toString()),
				exception.getMessage());
	}

	@Test
	public void givenRoomIsAvailable_whenCreate_thenLessonIsCreating() {
		Lesson actual = buildLesson();
		when(lessonDao
				.getByRoomAndDateAndTimeframe(actual.getRoom(), actual.getDate(), actual.getTimeframe()))
						.thenReturn(Optional.empty());

		lessonService.create(actual);

		verify(lessonDao).create(actual);
	}

	@Test
	public void givenGroupIsNotAvailable_whenCreate_thenThrowException() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(2L);
		Lesson actual = buildLesson();
		when(lessonDao.getByGroupIdAndDateAndTimeframe(1L, actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.of(lessonByCriteria));

		Exception exception = assertThrows(NotAvailableGroupException.class,
				() -> lessonService.create(actual));
		assertEquals("Other lesson was scheduled for the group AA-11 at the time 08:00, 2021-01-21",
				exception.getMessage());
	}

	@Test
	public void givenGroupIsAvailable_whenCreate_thenLessonIsCreating() {
		Lesson actual = buildLesson();
		when(lessonDao.getByGroupIdAndDateAndTimeframe(1L, actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.empty());

		lessonService.create(actual);

		verify(lessonDao).create(actual);
	}

	@Test
	public void givenRoomCapacityIsNotEnough_whenCreate_thenThrowException() {
		Lesson lesson = buildLesson();
		List<Student> students = Arrays.asList(new Student("Anna", "Maria"),
				new Student("Anatoly", "Deineka"),
				new Student("Alina", "Linkoln"),
				new Student("Homer", "Simpson"));
		when(studentDao.getByGroup(Mockito.any(Group.class))).thenReturn(students);

		Exception exception = assertThrows(LackOfCapacityException.class,
				() -> lessonService.create(lesson));
		assertEquals(format("Capacity of the room %s (%d seats) is not enough for %d students",
				lesson.getRoom().getName(),
				lesson.getRoom().getCapacity(),
				students.size()), exception.getMessage());
	}

	@Test
	public void givenRoomCapacityIsEnough_whenCreate_thenLessonIsCreating() {
		Lesson lesson = buildLesson();
		when(studentDao.getByGroup(Mockito.any(Group.class)))
				.thenReturn(Arrays.asList(new Student("Anna", "Maria"),
						new Student("Anatoly", "Deineka")));

		lessonService.create(lesson);

		verify(lessonDao).create(lesson);
	}

	@Test
	public void givenTeacherIsNotCompetentInCourse_whenCreate_thenThrowException() {
		Lesson lesson = buildLesson();
		lesson.setCourse(new Course("Law"));

		Exception exception = assertThrows(NotCompetentTeacherException.class,
				() -> lessonService.create(lesson));
		assertEquals(format("The teacher %s %s is incompetent to lecture course %s for the lesson",
				lesson.getTeacher().getName(),
				lesson.getTeacher().getSurname(),
				lesson.getCourse()), exception.getMessage());
	}

	@Test
	public void givenRoomIsNotAssignedForLessonCourse_whenCreate_thenThrowException() {
		Lesson lesson = buildLesson();
		lesson.setRoom(new Room("333"));

		Exception exception = assertThrows(IncompatibleRoomException.class,
				() -> lessonService.create(lesson));
		assertEquals(format("Course %s cannot be lectured in the room %s",
				lesson.getCourse().getName(),
				lesson.getRoom().getName()), exception.getMessage());
	}

	@Test
	public void givenDateIsOnSaturday_whenCreate_thenThrowException() {
		Lesson lesson = buildLesson();
		lesson.setDate(LocalDate.parse("2021-01-23"));

		Exception exception = assertThrows(IncompatibleDateException.class, () -> lessonService.create(lesson));
		assertEquals(format("The date the lesson appointed at is at the weekend (%s)", lesson.getDate().toString()),
				exception.getMessage());
	}

	@Test
	public void givenDateIsOnSunday_whenCreate_thenThrowException() {
		Lesson lesson = buildLesson();
		lesson.setDate(LocalDate.parse("2021-01-24"));

		Exception exception = assertThrows(IncompatibleDateException.class, () -> lessonService.create(lesson));
		assertEquals(format("The date the lesson appointed at is at the weekend (%s)", lesson.getDate().toString()),
				exception.getMessage());
	}

	@Test
	public void givenId_whenFindById_thenGetRightLesson() {
		Optional<Lesson> expected = Optional.of(buildLesson());
		when(lessonDao.findById(1L)).thenReturn(expected);

		Optional<Lesson> actual = lessonService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfLessons() {
		Lesson lesson = buildLesson();
		List<Lesson> expected = Arrays.asList(lesson);
		when(lessonDao.getAll()).thenReturn(expected);

		List<Lesson> actual = lessonService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenLesson_whenUpdate_thenLessonIsUpdating() {
		Lesson lesson = buildLesson();

		lessonService.update(lesson);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenTeacherIsNotAvailable_whenUpdate_thenThrowException() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(2L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		when(lessonDao.getByTeacherAndDateAndTimeframe(actual.getTeacher(), actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.of(lessonByCriteria));

		Exception exception = assertThrows(NotAvailableTeacherException.class,
				() -> lessonService.update(actual));
		assertEquals(format("Teacher %s %s is busy at the time %s, %s",
				actual.getTeacher().getName(),
				actual.getTeacher().getSurname(),
				actual.getTimeframe().getStartTime().toString(),
				actual.getDate().toString()),
				exception.getMessage());
	}

	@Test
	public void givenTeacherIsAvailable_whenUpdate_thenLessonIsUpdating() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(1L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		when(lessonDao.getByTeacherAndDateAndTimeframe(actual.getTeacher(),
				actual.getDate(),
				actual.getTimeframe())).thenReturn(Optional.of(lessonByCriteria));

		lessonService.update(actual);

		verify(lessonDao).update(actual);
	}

	@Test
	public void givenRoomIsNotAvailable_whenUpdate_thenThrowException() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(2L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		when(lessonDao.getByRoomAndDateAndTimeframe(actual.getRoom(), actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.of(lessonByCriteria));

		Exception exception = assertThrows(NotAvailableRoomException.class,
				() -> lessonService.update(actual));
		assertEquals(format("Room %s is occupied at the time %s, %s",
				actual.getRoom().getName(),
				actual.getTimeframe().getStartTime().toString(),
				actual.getDate().toString()),
				exception.getMessage());
	}

	@Test
	public void givenRoomIsAvailable_whenUpdate_thenLessonIsUpdating() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(1L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		when(lessonDao
				.getByRoomAndDateAndTimeframe(actual.getRoom(), actual.getDate(), actual.getTimeframe()))
						.thenReturn(Optional.of(lessonByCriteria));

		lessonService.update(actual);

		verify(lessonDao).update(actual);
	}

	@Test
	public void givenGroupIsNotAvailable_whenUpdate_thenThrowException() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(2L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		when(lessonDao.getByGroupIdAndDateAndTimeframe(1L, actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.of(lessonByCriteria));

		Exception exception = assertThrows(NotAvailableGroupException.class,
				() -> lessonService.update(actual));
		assertEquals("Other lesson was scheduled for the group AA-11 at the time 08:00, 2021-01-21",
				exception.getMessage());
	}

	@Test
	public void givenGroupIsAvailable_whenUpdate_thenLessonIsUpdating() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(1L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		when(lessonDao.getByGroupIdAndDateAndTimeframe(1L, actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.of(lessonByCriteria));

		lessonService.update(actual);

		verify(lessonDao).update(actual);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenLessonIsDeleting() {
		when(lessonDao.findById(1L)).thenReturn(Optional.of(buildLesson()));

		lessonService.deleteById(1L);

		verify(lessonDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenThrowException() {
		when(lessonDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> lessonService.deleteById(1L));
		assertEquals("Cannot find lesson by id: 1", exception.getMessage());
	}

	private Lesson buildLesson() {
		Room room = new Room("111");
		room.setId(1L);
		room.setCapacity(3);
		Course course = new Course("Art");
		course.setId(1L);
		course.setRooms(new HashSet<>(Arrays.asList(room)));
		LocalDate date = LocalDate.parse("2021-01-21");
		Group group = new Group("AA-11");
		group.setId(1L);
		Set<Group> groups = new HashSet<>(Arrays.asList(group));
		Teacher teacher = new Teacher("Homer", "Simpson");
		teacher.setId(1L);
		teacher.setCourses(new HashSet<>(Arrays.asList(course)));
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequence(1);
		timeframe.setStartTime(LocalTime.parse("08:00"));
		timeframe.setEndTime(LocalTime.parse("09:20"));
		Lesson lesson = new Lesson();
		lesson.setCourse(course);
		lesson.setDate(date);
		lesson.setGroups(groups);
		lesson.setRoom(room);
		lesson.setTeacher(teacher);
		lesson.setTimeframe(timeframe);
		return lesson;
	}
}
