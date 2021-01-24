package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
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
		Course course = getStandardCourse();

		courseService.create(course);

		verify(courseDao).create(course);
	}

	@Test
	public void givenNameIsNull_whenCreate_thenCourseIsNotCreating() {
		Course course = getStandardCourse();
		course.setName(null);

		courseService.create(course);

		verify(courseDao, never()).create(course);
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenCourseIsNotCreating() {
		Course course = getStandardCourse();
		course.setName("");

		courseService.create(course);

		verify(courseDao, never()).create(course);
	}

	@Test
	public void givenRoomsIsEmpty_whenCreate_thenCourseIsNotCreating() {
		Course course = getStandardCourse();
		course.setRooms(new HashSet<>());

		courseService.create(course);

		verify(courseDao, never()).create(course);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Course expected = getStandardCourse();
		when(courseDao.findById(1L)).thenReturn(expected);

		Course actual = courseService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Course> expected = Arrays.asList(getStandardCourse());
		when(courseDao.getAll()).thenReturn(expected);

		List<Course> actual = courseService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenCourse_whenUpdate_thenCourseIsUpdating() {
		Course course = getStandardCourse();

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
		Course course = getStandardCourse();
		Room room = new Room("333");
		when(courseDao.findById(1L)).thenReturn(course);
		when(roomDao.findById(anyLong())).thenReturn(room);

		courseService.addRoomById(1L, anyLong());

		verify(courseDao).update(course);
	}

	@Test
	public void givenRoom_whenDeleteRoomById_thenDataIsUpdating() {
		Course course = getStandardCourse();
		Room room = new Room("111");
		room.setId(1L);
		when(courseDao.findById(1L)).thenReturn(course);
		when(roomDao.findById(1L)).thenReturn(room);

		courseService.removeRoomById(1L, 1L);

		verify(courseDao).update(course);
	}

	@Test
	public void givenId_whenGetCoursesByRoomId_thenGetRightData() {
		List<Course> expected = Arrays.asList(getStandardCourse());
		when(courseDao.getCoursesByRoomId(anyLong())).thenReturn(expected);

		List<Course> actual = courseService.getCoursesByRoomId(anyLong());

		assertEquals(expected, actual);
	}

	private Course getStandardCourse() {
		Room room1 = new Room("111");
		room1.setId(1L);
		Room room2 = new Room("222");
		room2.setId(2L);
		Course course = new Course("Art");
		course.setId(1L);
		course.setRooms(new HashSet<>(Arrays.asList(room1, room2)));
		return course;
	}
}
