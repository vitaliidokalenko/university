package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
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
	public void givenTeacherIsNotAvailable_whenCreate_thenLessonIsNotCreating() {
		Optional<Lesson> lessonByCriteria = Optional.of(buildLesson());
		lessonByCriteria.get().setId(2L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		when(lessonDao.getByTeacherAndDateAndTimeframe(actual.getTeacher(),
				LocalDate.parse("2021-01-21"),
				actual.getTimeframe()))
						.thenReturn(lessonByCriteria);

		lessonService.create(actual);

		verify(lessonDao, never()).create(actual);
	}

	@Test
	public void givenRoomIsNotAvailable_whenCreate_thenLessonIsNotCreating() {
		Optional<Lesson> lessonByCriteria = Optional.of(buildLesson());
		lessonByCriteria.get().setId(2L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		when(lessonDao
				.getByRoomAndDateAndTimeframe(actual.getRoom(), LocalDate.parse("2021-01-21"), actual.getTimeframe()))
						.thenReturn(lessonByCriteria);

		lessonService.create(actual);

		verify(lessonDao, never()).create(actual);
	}

	@Test
	public void givenGroupIsNotAvailable_whenCreate_thenLessonIsNotCreating() {
		Optional<Lesson> lessonByCriteria = Optional.of(buildLesson());
		lessonByCriteria.get().setId(2L);
		Lesson actual = buildLesson();
		actual.setId(1L);
		when(lessonDao.getByGroupIdAndDateAndTimeframe(1L, LocalDate.parse("2021-01-21"), actual.getTimeframe()))
				.thenReturn(lessonByCriteria);

		lessonService.create(actual);

		verify(lessonDao, never()).create(actual);
	}

	@Test
	public void givenRoomCapacityIsNotCompatible_whenCreate_thenLessonIsNotCreating() {
		Lesson lesson = buildLesson();
		when(studentDao.getByGroup(Mockito.any(Group.class)))
				.thenReturn(Arrays.asList(new Student("Anna", "Maria"),
						new Student("Anatoly", "Deineka"),
						new Student("Alina", "Linkoln"),
						new Student("Homer", "Simpson")));

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	public void givenRoomCapacityIsCompatible_whenCreate_thenLessonIsCreating() {
		Lesson lesson = buildLesson();
		when(studentDao.getByGroup(Mockito.any(Group.class)))
				.thenReturn(Arrays.asList(new Student("Anna", "Maria"),
						new Student("Anatoly", "Deineka")));

		lessonService.create(lesson);

		verify(lessonDao).create(lesson);
	}

	@Test
	public void givenTeacherCourseIsNotCompatible_whenCreate_thenLessonIsNotCreating() {
		Lesson lesson = buildLesson();
		lesson.setCourse(new Course("Law"));

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	public void givenCourseRoomIsNotCompatible_whenCreate_thenLessonIsNotCreating() {
		Lesson lesson = buildLesson();
		lesson.setRoom(new Room("333"));

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	public void givenDateIsOnSaturday_whenCreate_thenLessonIsNotCreating() {
		Lesson lesson = buildLesson();
		lesson.setDate(LocalDate.parse("2021-01-23"));

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	public void givenDateIsOnSunday_whenCreate_thenLessonIsNotCreating() {
		Lesson lesson = buildLesson();
		lesson.setDate(LocalDate.parse("2021-01-24"));

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Optional<Lesson> expected = Optional.of(buildLesson());
		when(lessonDao.findById(1L)).thenReturn(expected);

		Optional<Lesson> actual = lessonService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
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
	public void givenEntityIsPresent_whenDeleteById_thenLessonIsDeleting() {
		when(lessonDao.findById(1L)).thenReturn(Optional.of(buildLesson()));

		lessonService.deleteById(1L);

		verify(lessonDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenLessonIsNotDeleting() {
		when(lessonDao.findById(1L)).thenReturn(Optional.empty());

		lessonService.deleteById(1L);

		verify(lessonDao, never()).deleteById(1L);
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
		timeframe.setSequance(1);
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
