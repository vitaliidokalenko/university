package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

	@Mock
	private CourseDao courseDao;
	@Mock
	private RoomDao roomDao;

	@InjectMocks
	private CourseService courseService;

	@Test
	public void givenCourse_whenCreate_thenCourseIsCreating() {
		Course course = new Course("Art");

		courseService.create(course);

		verify(courseDao).create(course);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Course expected = new Course("Art");
		Long id = 1L;
		expected.setId(id);
		when(courseDao.findById(id)).thenReturn(expected);

		Course actual = courseService.findById(id);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Course> expected = Arrays.asList(new Course("Art"));
		when(courseDao.getAll()).thenReturn(expected);

		List<Course> actual = courseService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenCourse_whenUpdate_thenCourseIsUpdating() {
		Course course = new Course("Art");

		courseService.update(course);

		verify(courseDao).update(course);
	}

	@Test
	public void givenId_whenDeleteById_thenCourseIsDeleting() {
		courseService.deleteById(anyLong());

		verify(courseDao).deleteById(anyLong());
	}

	@Test
	public void givenRoom_whenAddRoomById_thenDataIsUpdating() {
		Course course = new Course("Art");
		Long id = 1L;
		Room room = new Room("222");
		when(courseDao.findById(id)).thenReturn(course);
		when(roomDao.findById(id)).thenReturn(room);

		courseService.addRoomById(id, id);

		verify(courseDao).update(course);
	}

	@Test
	public void givenRoom_whenDeleteRoomById_thenDataIsUpdating() {
		Course course = new Course("Art");
		Long id = 1L;
		Room room = new Room("222");
		List<Room> rooms = Arrays.asList(room);
		course.setRooms(new HashSet<>(rooms));
		when(courseDao.findById(id)).thenReturn(course);
		when(roomDao.findById(id)).thenReturn(room);

		courseService.removeRoomById(id, id);

		verify(courseDao).update(course);
	}

	@Test
	public void givenId_whenGetCoursesByRoomId_thenGetRightData() {
		List<Course> expected = Arrays.asList(new Course("Art"));
		when(courseDao.getCoursesByRoomId(anyLong())).thenReturn(expected);

		List<Course> actual = courseService.getCoursesByRoomId(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetCoursesByStudentId_thenGetRightData() {
		List<Course> expected = Arrays.asList(new Course("Art"));
		when(courseDao.getCoursesByStudentId(anyLong())).thenReturn(expected);

		List<Course> actual = courseService.getCoursesByStudentId(anyLong());

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetCoursesByTeacherId_thenGetRightData() {
		List<Course> expected = Arrays.asList(new Course("Art"));
		when(courseDao.getCoursesByTeacherId(anyLong())).thenReturn(expected);

		List<Course> actual = courseService.getCoursesByTeacherId(anyLong());

		assertEquals(expected, actual);
	}
}
