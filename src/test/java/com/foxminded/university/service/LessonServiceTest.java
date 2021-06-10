package com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.exception.NotAvailableGroupException;
import com.foxminded.university.service.exception.NotAvailableRoomException;
import com.foxminded.university.service.exception.NotAvailableTeacherException;
import com.foxminded.university.service.exception.NotCompetentTeacherForCourseException;
import com.foxminded.university.service.exception.NotEnoughRoomCapacityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotFoundSubstituteTeacherException;
import com.foxminded.university.service.exception.NotSuitableRoomForCourseException;
import com.foxminded.university.service.exception.NotWeekDayException;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

	@Mock
	private LessonDao lessonDao;
	@Mock
	private StudentDao studentDao;
	@Mock
	private TeacherDao teacherDao;
	@InjectMocks
	private LessonService lessonService;

	@Test
	public void givenLesson_whenCreate_thenLessonIsCreating() {
		Lesson lesson = buildLesson();

		lessonService.create(lesson);

		verify(lessonDao).save(lesson);
	}

	@Test
	public void givenTeacherIsNotAvailable_whenCreate_thenNotAvailableTeacherExceptionThrown() {
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

		verify(lessonDao).save(actual);
	}

	@Test
	public void givenRoomIsNotAvailable_whenCreate_thenNotAvailableRoomExceptionThrown() {
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

		verify(lessonDao).save(actual);
	}

	@Test
	public void givenGroupIsNotAvailable_whenCreate_thenNotAvailableGroupExceptionThrown() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(2L);
		Lesson actual = buildLesson();
		Group group = Group.builder().id(1L).name("AA-11").build();
		when(lessonDao.getByGroupsAndDateAndTimeframe(group, actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.of(lessonByCriteria));

		Exception exception = assertThrows(NotAvailableGroupException.class,
				() -> lessonService.create(actual));
		assertEquals("Other lesson was scheduled for the group AA-11 at the time 08:00, 2021-01-21",
				exception.getMessage());
	}

	@Test
	public void givenGroupIsAvailable_whenCreate_thenLessonIsCreating() {
		Lesson actual = buildLesson();
		Group group = Group.builder().id(1L).name("AA-11").build();
		when(lessonDao.getByGroupsAndDateAndTimeframe(group, actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.empty());

		lessonService.create(actual);

		verify(lessonDao).save(actual);
	}

	@Test
	public void givenRoomCapacityIsNotEnough_whenCreate_thenNotEnoughRoomCapacityExceptionThrown() {
		Lesson lesson = buildLesson();
		List<Student> students = List.of(Student.builder().name("Anna").surname("Maria").build(),
				Student.builder().name("Anatoly").surname("Deineka").build(),
				Student.builder().name("Alina").surname("Linkoln").build(),
				Student.builder().name("Homer").surname("Simpson").build());
		when(studentDao.getByGroup(Mockito.any(Group.class))).thenReturn(students);

		Exception exception = assertThrows(NotEnoughRoomCapacityException.class,
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
				.thenReturn(List.of(Student.builder().name("Anna").surname("Maria").build(),
						Student.builder().name("Anatoly").surname("Deineka").build()));

		lessonService.create(lesson);

		verify(lessonDao).save(lesson);
	}

	@Test
	public void givenTeacherIsNotCompetentInCourse_whenCreate_thenNotCompetentTeacherForCourseExceptionThrown() {
		Lesson lesson = buildLesson();
		lesson.setCourse(Course.builder().id(3L).name("Biology").build());

		Exception exception = assertThrows(NotCompetentTeacherForCourseException.class,
				() -> lessonService.create(lesson));
		assertEquals(format("The teacher %s %s is incompetent to lecture course %s",
				lesson.getTeacher().getName(),
				lesson.getTeacher().getSurname(),
				lesson.getCourse()), exception.getMessage());
	}

	@Test
	public void givenRoomIsNotAssignedForLessonCourse_whenCreate_thenNotSuitableRoomForCourseExceptionThrown() {
		Lesson lesson = buildLesson();
		lesson.setRoom(Room.builder().id(3L).name("333").build());

		Exception exception = assertThrows(NotSuitableRoomForCourseException.class,
				() -> lessonService.create(lesson));
		assertEquals(format("Course %s cannot be lectured in the room %s",
				lesson.getCourse().getName(),
				lesson.getRoom().getName()), exception.getMessage());
	}

	@Test
	public void givenDateIsOnSaturday_whenCreate_thenNotWeekDayExceptionThrown() {
		Lesson lesson = buildLesson();
		lesson.setDate(LocalDate.parse("2021-01-23"));

		Exception exception = assertThrows(NotWeekDayException.class, () -> lessonService.create(lesson));
		assertEquals(format("Lesson cannot be appointed at the weekend (%s)", lesson.getDate().toString()),
				exception.getMessage());
	}

	@Test
	public void givenDateIsOnSunday_whenCreate_thenNotWeekDayExceptionThrown() {
		Lesson lesson = buildLesson();
		lesson.setDate(LocalDate.parse("2021-01-24"));

		Exception exception = assertThrows(NotWeekDayException.class, () -> lessonService.create(lesson));
		assertEquals(format("Lesson cannot be appointed at the weekend (%s)", lesson.getDate().toString()),
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
	public void givenLesson_whenUpdate_thenLessonIsUpdating() {
		Lesson lesson = buildLesson();

		lessonService.update(lesson);

		verify(lessonDao).save(lesson);
	}

	@Test
	public void givenTeacherIsNotAvailable_whenUpdate_thenNotAvailableTeacherExceptionThrown() {
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

		verify(lessonDao).save(actual);
	}

	@Test
	public void givenRoomIsNotAvailable_whenUpdate_thenNotAvailableRoomExceptionThrown() {
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

		verify(lessonDao).save(actual);
	}

	@Test
	public void givenGroupIsNotAvailable_whenUpdate_thenNotAvailableGroupExceptionThrown() {
		Lesson lessonByCriteria = buildLesson();
		lessonByCriteria.setId(2L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		Group group = Group.builder().id(1L).name("AA-11").build();
		when(lessonDao.getByGroupsAndDateAndTimeframe(group, actual.getDate(), actual.getTimeframe()))
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
		Group group = Group.builder().id(1L).name("AA-11").build();
		when(lessonDao.getByGroupsAndDateAndTimeframe(group, actual.getDate(), actual.getTimeframe()))
				.thenReturn(Optional.of(lessonByCriteria));

		lessonService.update(actual);

		verify(lessonDao).save(actual);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenLessonIsDeleting() {
		Lesson lesson = buildLesson();
		when(lessonDao.findById(1L)).thenReturn(Optional.of(lesson));

		lessonService.deleteById(1L);

		verify(lessonDao).delete(lesson);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenNotFoundEntityExceptionThrown() {
		when(lessonDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> lessonService.deleteById(1L));
		assertEquals("Cannot find lesson by id: 1", exception.getMessage());
	}

	@Test
	public void whenGetAllPage_thenGetRightLessons() {
		Page<Lesson> expected = new PageImpl<>(List.of(buildLesson()));
		when(lessonDao.findAll(PageRequest.of(0, 1))).thenReturn(expected);

		Page<Lesson> actual = lessonService.getAllPage(PageRequest.of(0, 1));

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetByTeacherAndDateBetween_thenGetRightListOfLessons() {
		Lesson lesson = buildLesson();
		List<Lesson> expected = List.of(lesson);
		when(lessonDao.getByTeacherAndDateBetween(lesson.getTeacher(), LocalDate.parse("2021-01-21"), LocalDate.parse("2021-01-21")))
				.thenReturn(expected);

		List<Lesson> actual = lessonService
				.getByTeacherAndDateBetween(lesson.getTeacher(), LocalDate.parse("2021-01-21"), LocalDate.parse("2021-01-21"));

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetByGroupAndDateBetween_thenGetRightListOfLessons() {
		Lesson lesson = buildLesson();
		List<Lesson> expected = List.of(lesson);
		Group group = Group.builder().id(1L).name("AA-11").build();
		when(lessonDao.getByGroupsAndDateBetween(group, LocalDate.parse("2021-01-21"), LocalDate.parse("2021-01-21")))
				.thenReturn(expected);

		List<Lesson> actual = lessonService
				.getByGroupAndDateBetween(group, LocalDate.parse("2021-01-21"), LocalDate.parse("2021-01-21"));

		assertEquals(expected, actual);
	}

	@Test
	public void givenSubstituteTeachersIsNull_whenReplaceTeacherByDateBetween_thenLessonsIsUpdating() {
		LocalDate startDate = LocalDate.parse("2021-01-20");
		LocalDate endDate = LocalDate.parse("2021-01-22");
		Lesson lesson = buildLesson();
		List<Lesson> lessons = List.of(lesson);
		Course course = Course.builder().id(1L).name("Art").build();
		Teacher substituteTeacher = buildTeacher();
		substituteTeacher.setId(2L);
		when(lessonDao.getByTeacherAndDateBetween(lesson.getTeacher(), startDate, endDate))
				.thenReturn(lessons);
		when(teacherDao.getByCourses(course)).thenReturn(List.of(substituteTeacher));
		when(lessonDao.getByTeacherAndDateAndTimeframe(substituteTeacher, lesson.getDate(), lesson.getTimeframe()))
				.thenReturn(Optional.empty());

		lessonService.replaceTeacherByDateBetween(buildTeacher(), startDate, endDate, null);

		verify(lessonDao).save(lesson);
	}

	@Test
	public void givenSubstituteTeachersIsNotNull_whenReplaceTeacherByDateBetween_thenLessonsIsUpdating() {
		LocalDate startDate = LocalDate.parse("2021-01-20");
		LocalDate endDate = LocalDate.parse("2021-01-22");
		Lesson lesson = buildLesson();
		List<Lesson> lessons = List.of(lesson);
		Teacher substituteTeacher = buildTeacher();
		substituteTeacher.setId(2L);
		when(lessonDao.getByTeacherAndDateBetween(lesson.getTeacher(), startDate, endDate))
				.thenReturn(lessons);
		when(teacherDao.findById(1L)).thenReturn(Optional.of(substituteTeacher));
		when(lessonDao.getByTeacherAndDateAndTimeframe(substituteTeacher, lesson.getDate(), lesson.getTimeframe()))
				.thenReturn(Optional.empty());

		lessonService.replaceTeacherByDateBetween(buildTeacher(), startDate, endDate, List.of(1L));

		verify(lessonDao).save(lesson);
	}

	@Test
	public void givenSubstituteTeachersIsNotAvailable_whenReplaceTeacherByDateBetween_thenNotFoundSubstituteTeacherExceptionThrown() {
		LocalDate startDate = LocalDate.parse("2021-01-20");
		LocalDate endDate = LocalDate.parse("2021-01-22");
		Lesson lesson = buildLesson();
		List<Lesson> lessons = List.of(lesson);
		Course course = Course.builder().id(1L).name("Art").build();
		Teacher replacedTeacher = buildTeacher();
		Teacher substituteTeacher = buildTeacher();
		substituteTeacher.setId(2L);
		when(lessonDao.getByTeacherAndDateBetween(replacedTeacher, startDate, endDate))
				.thenReturn(lessons);
		when(teacherDao.getByCourses(course))
				.thenReturn(List.of(substituteTeacher));
		when(lessonDao.getByTeacherAndDateAndTimeframe(substituteTeacher, lesson.getDate(), lesson.getTimeframe()))
				.thenReturn(Optional.of(buildLesson()));

		Exception exception = assertThrows(NotFoundSubstituteTeacherException.class,
				() -> lessonService.replaceTeacherByDateBetween(replacedTeacher, startDate, endDate, null));
		assertEquals(
				"Substitute teacher was not found for the lesson id: 1, course: Art, date: 2021-01-21, start time: 08:00",
				exception.getMessage());
	}

	private Lesson buildLesson() {
		return Lesson.builder()
				.id(1L)
				.course(Course.builder().id(1L).name("Art").rooms(Set.of(buildRoom())).build())
				.date(LocalDate.parse("2021-01-21"))
				.groups(Set.of(Group.builder().id(1L).name("AA-11").build()))
				.room(buildRoom())
				.teacher(buildTeacher())
				.timeframe(Timeframe.builder()
						.id(1L)
						.sequence(1)
						.startTime(LocalTime.parse("08:00"))
						.endTime(LocalTime.parse("09:20"))
						.build())
				.build();
	}

	private Teacher buildTeacher() {
		return Teacher.builder()
				.id(1L)
				.name("Homer")
				.surname("Simpson")
				.courses(Set.of(Course.builder().id(1L).name("Art").build(),
						Course.builder().id(2L).name("Law").build()))
				.gender(Gender.MALE)
				.build();
	}

	private Room buildRoom() {
		return Room.builder().id(1L).name("111").capacity(3).build();
	}
}
