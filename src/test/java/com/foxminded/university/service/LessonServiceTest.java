package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.TimeframeDao;
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
	private GroupDao groupDao;
	@Mock
	private TeacherDao teacherDao;
	@Mock
	private CourseDao courseDao;
	@Mock
	private RoomDao roomDao;
	@Mock
	private TimeframeDao timeframeDao;
	@Mock
	private StudentDao studentDao;

	@InjectMocks
	private LessonService lessonService;

	@Test
	public void givenLesson_whenCreate_thenLessonIsCreating() {
		Lesson lesson = getStandardLesson();

		lessonService.create(lesson);

		verify(lessonDao).create(lesson);
	}

	@Test
	public void givenTeacherIsNotAvailable_whenCreate_thenLessonIsNotCreating() {
		Lesson lessonByTeacher = getStandardLesson();
		Lesson actualLesson = getStandardLesson();
		when(lessonDao.getLessonsByTeacher(actualLesson.getTeacher())).thenReturn(Arrays.asList(lessonByTeacher));

		lessonService.create(actualLesson);

		verify(lessonDao, never()).create(actualLesson);
	}

	@Test
	public void givenRoomIsNotAvailable_whenCreate_thenLessonIsNotCreating() {
		Lesson lessonByRoom = getStandardLesson();
		Lesson actualLesson = getStandardLesson();
		when(lessonDao.getLessonsByRoom(actualLesson.getRoom())).thenReturn(Arrays.asList(lessonByRoom));

		lessonService.create(actualLesson);

		verify(lessonDao, never()).create(actualLesson);
	}

	@Test
	public void givenGroupIsNotAvailable_whenCreate_thenLessonIsNotCreating() {
		Lesson lessonByGroup = getStandardLesson();
		Lesson actualLesson = getStandardLesson();
		when(lessonDao.getLessonsByGroupId(1L)).thenReturn(Arrays.asList(lessonByGroup));

		lessonService.create(actualLesson);

		verify(lessonDao, never()).create(actualLesson);
	}

	@Test
	public void givenRoomCapacityIsNotCompatible_whenCreate_thenLessonIsNotCreating() {
		Lesson lesson = getStandardLesson();
		lesson.getRoom().setCapacity(3);
		when(studentDao.getStudentsByGroup(Mockito.any(Group.class)))
				.thenReturn(Arrays.asList(new Student("Anna", "Maria"),
						new Student("Anatoly", "Deineka"),
						new Student("Alina", "Linkoln"),
						new Student("Homer", "Simpson")));

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	public void givenRoomCapacityIsCompatible_whenCreate_thenLessonIsCreating() {
		Lesson lesson = getStandardLesson();
		lesson.getRoom().setCapacity(4);
		when(studentDao.getStudentsByGroup(Mockito.any(Group.class)))
				.thenReturn(Arrays.asList(new Student("Anna", "Maria"),
						new Student("Anatoly", "Deineka"),
						new Student("Alina", "Linkoln"),
						new Student("Homer", "Simpson")));

		lessonService.create(lesson);

		verify(lessonDao).create(lesson);
	}

	@Test
	public void givenTeacherCourseIsNotCompatible_whenCreate_thenLessonIsNotCreating() {
		Lesson lesson = getStandardLesson();
		lesson.setCourse(new Course("Law"));

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	public void givenCourseRoomIsNotCompatible_whenCreate_thenLessonIsNotCreating() {
		Lesson lesson = getStandardLesson();
		lesson.setRoom(new Room("333"));

		lessonService.create(lesson);

		verify(lessonDao, never()).create(lesson);
	}

	@Test
	public void givenCourseRoomIsCompatible_whenCreate_thenLessonIsCreating() {
		Room room = new Room("333");
		Lesson lesson = getStandardLesson();
		lesson.setRoom(room);
		lesson.getCourse().getRooms().add(room);

		lessonService.create(lesson);

		verify(lessonDao).create(lesson);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Lesson expected = getStandardLesson();
		when(lessonDao.findById(anyLong())).thenReturn(expected);

		Lesson actual = lessonService.findById(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		Lesson lesson = getStandardLesson();
		List<Lesson> expected = Arrays.asList(lesson);
		when(lessonDao.getAll()).thenReturn(expected);

		List<Lesson> actual = lessonService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenLesson_whenUpdate_thenLessonIsUpdating() {
		Lesson lesson = getStandardLesson();

		lessonService.update(lesson);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenLessonIsDeleting() {
		when(lessonDao.findById(1L)).thenReturn(getStandardLesson());

		lessonService.deleteById(1L);

		verify(lessonDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenLessonIsNotDeleting() {
		when(lessonDao.findById(1L)).thenThrow(EmptyResultDataAccessException.class);

		lessonService.deleteById(1L);

		verify(lessonDao, never()).deleteById(1L);
	}

	@Test
	public void givenGroup_whenAddGroupById_thenDataIsUpdating() {
		Lesson lesson = getStandardLesson();
		Group group = new Group("DD-44");
		when(lessonDao.findById(1L)).thenReturn(lesson);
		when(groupDao.findById(anyLong())).thenReturn(group);

		lessonService.addGroupById(1L, anyLong());

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenGroup_whenRemoveGroupById_thenDataIsUpdating() {
		Lesson lesson = getStandardLesson();
		Group group2 = new Group("AA-22");
		group2.setId(2L);
		lesson.getGroups().add(group2);
		when(lessonDao.findById(1L)).thenReturn(lesson);
		when(groupDao.findById(2L)).thenReturn(group2);

		lessonService.removeGroupById(1L, 2L);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenSetTeacherById_thenDataIsUpdating() {
		Lesson lesson = getStandardLesson();
		Teacher teacher = new Teacher("Anatoly", "Garriga");
		when(lessonDao.findById(1L)).thenReturn(lesson);
		when(teacherDao.findById(anyLong())).thenReturn(teacher);

		lessonService.setTeacherById(1L, anyLong());

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenSetCourseById_thenDataIsUpdating() {
		Lesson lesson = getStandardLesson();
		Course course = new Course("Philosophy");
		when(lessonDao.findById(1L)).thenReturn(lesson);
		when(courseDao.findById(anyLong())).thenReturn(course);

		lessonService.setCourseById(1L, anyLong());

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenSetRoomById_thenDataIsUpdating() {
		Lesson lesson = getStandardLesson();
		Room room = new Room("555");
		when(lessonDao.findById(1L)).thenReturn(lesson);
		when(roomDao.findById(anyLong())).thenReturn(room);

		lessonService.setRoomById(1L, anyLong());

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenSetTimeframeById_thenDataIsUpdating() {
		Lesson lesson = getStandardLesson();
		Timeframe timeframe = new Timeframe();
		timeframe.setSequance(5);
		when(lessonDao.findById(1L)).thenReturn(lesson);
		when(timeframeDao.findById(anyLong())).thenReturn(timeframe);

		lessonService.setTimeframeById(1L, anyLong());

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenGetLessonsByGroupId_thenGetRightData() {
		Lesson lesson = getStandardLesson();
		List<Lesson> expected = Arrays.asList(lesson);
		when(lessonDao.getLessonsByGroupId(anyLong())).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByGroupId(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetLessonsByTimeframe_thenGetRightData() {
		Lesson lesson = getStandardLesson();
		List<Lesson> expected = Arrays.asList(lesson);
		Timeframe timeframe = new Timeframe();
		when(lessonDao.getLessonsByTimeframe(timeframe)).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByTimeframe(timeframe);

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetLessonsByCourse_thenGetRightData() {
		Lesson lesson = getStandardLesson();
		List<Lesson> expected = Arrays.asList(lesson);
		Course course = new Course("Art");
		when(lessonDao.getLessonsByCourse(course)).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByCourse(course);

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetLessonsByTeacher_thenGetRightData() {
		Lesson lesson = getStandardLesson();
		List<Lesson> expected = Arrays.asList(lesson);
		Teacher teacher = new Teacher("Ivan", "Milevskii");
		when(lessonDao.getLessonsByTeacher(teacher)).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByTeacher(teacher);

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetLessonsByRoom_thenGetRightData() {
		Lesson lesson = getStandardLesson();
		List<Lesson> expected = Arrays.asList(lesson);
		Room room = new Room("111");
		when(lessonDao.getLessonsByRoom(room)).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByRoom(room);

		assertEquals(expected, actual);
	}

	private Lesson getStandardLesson() {
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
