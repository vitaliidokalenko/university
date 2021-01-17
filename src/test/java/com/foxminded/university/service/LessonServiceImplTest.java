package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.impl.LessonServiceImpl;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class LessonServiceImplTest {

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

	@InjectMocks
	private LessonServiceImpl lessonService;

	@Test
	public void givenLesson_whenCreate_thenLessonIsCreating() {
		Lesson lesson = new Lesson();
		lesson.setId(1L);

		lessonService.create(lesson);

		verify(lessonDao).create(lesson);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Lesson expected = new Lesson();
		Long id = 1L;
		expected.setId(id);
		List<Group> groups = Arrays.asList(new Group("AA-11"));
		when(lessonDao.findById(id)).thenReturn(expected);
		when(groupDao.getGroupsByLessonId(id)).thenReturn(groups);

		Lesson actual = lessonService.findById(id);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		List<Lesson> expected = Arrays.asList(lesson);
		when(lessonDao.getAll()).thenReturn(expected);

		List<Lesson> actual = lessonService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenLesson_whenUpdate_thenLessonIsUpdating() {
		Lesson lesson = new Lesson();
		lesson.setId(1L);

		lessonService.update(lesson);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenDeleteById_thenLessonIsDeleting() {
		lessonService.deleteById(anyLong());

		verify(lessonDao).deleteById(anyLong());
	}

	@Test
	public void givenGroup_whenAddGroupById_thenDataIsUpdating() {
		Lesson lesson = new Lesson();
		Long id = 1L;
		lesson.setId(id);
		List<Group> groups = Arrays.asList(new Group("AA-11"));
		Group group = new Group("AA-22");
		when(lessonDao.findById(id)).thenReturn(lesson);
		when(groupDao.getGroupsByLessonId(id)).thenReturn(groups);
		when(groupDao.findById(id)).thenReturn(group);

		lessonService.addGroupById(id, id);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenGroup_whenRemoveGroupById_thenDataIsUpdating() {
		Lesson lesson = new Lesson();
		Long id = 1L;
		lesson.setId(id);
		Group group = new Group("AA-22");
		List<Group> groups = Arrays.asList(group);
		when(lessonDao.findById(id)).thenReturn(lesson);
		when(groupDao.getGroupsByLessonId(id)).thenReturn(groups);
		when(groupDao.findById(id)).thenReturn(group);

		lessonService.removeGroupById(id, id);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenSetTeacherById_thenDataIsUpdating() {
		Long id = 1L;
		Lesson lesson = new Lesson();
		Teacher teacher = new Teacher("Anatoly", "Garriga");
		when(lessonDao.findById(id)).thenReturn(lesson);
		when(teacherDao.findById(id)).thenReturn(teacher);

		lessonService.setTeacherById(id, id);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenSetCourseById_thenDataIsUpdating() {
		Long id = 1L;
		Lesson lesson = new Lesson();
		Course course = new Course("Art");
		when(lessonDao.findById(id)).thenReturn(lesson);
		when(courseDao.findById(id)).thenReturn(course);

		lessonService.setCourseById(id, id);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenSetRoomById_thenDataIsUpdating() {
		Long id = 1L;
		Lesson lesson = new Lesson();
		Room room = new Room("111");
		when(lessonDao.findById(id)).thenReturn(lesson);
		when(roomDao.findById(id)).thenReturn(room);

		lessonService.setRoomById(id, id);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenSetTimeframeById_thenDataIsUpdating() {
		Long id = 1L;
		Lesson lesson = new Lesson();
		Timeframe timeframe = new Timeframe();
		when(lessonDao.findById(id)).thenReturn(lesson);
		when(timeframeDao.findById(id)).thenReturn(timeframe);

		lessonService.setTimeframeById(id, id);

		verify(lessonDao).update(lesson);
	}

	@Test
	public void givenId_whenGetLessonsByGroupId_thenGetRightData() {
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		List<Lesson> expected = Arrays.asList(lesson);
		when(lessonDao.getLessonsByGroupId(anyLong())).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByGroupId(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetLessonsByTimeframe_thenGetRightData() {
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		List<Lesson> expected = Arrays.asList(lesson);
		Timeframe timeframe = new Timeframe();
		when(lessonDao.getLessonsByTimeframe(timeframe)).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByTimeframe(timeframe);

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetLessonsByCourse_thenGetRightData() {
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		List<Lesson> expected = Arrays.asList(lesson);
		Course course = new Course("Art");
		when(lessonDao.getLessonsByCourse(course)).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByCourse(course);

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetLessonsByTeacher_thenGetRightData() {
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		List<Lesson> expected = Arrays.asList(lesson);
		Teacher teacher = new Teacher("Ivan", "Milevskii");
		when(lessonDao.getLessonsByTeacher(teacher)).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByTeacher(teacher);

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetLessonsByRoom_thenGetRightData() {
		Lesson lesson = new Lesson();
		lesson.setId(1L);
		List<Lesson> expected = Arrays.asList(lesson);
		Room room = new Room("111");
		when(lessonDao.getLessonsByRoom(room)).thenReturn(expected);

		List<Lesson> actual = lessonService.getLessonsByRoom(room);

		assertEquals(expected, actual);
	}
}
