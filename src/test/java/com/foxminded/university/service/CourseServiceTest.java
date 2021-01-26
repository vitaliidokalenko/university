package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

	@Mock
	private CourseDao courseDao;

	@InjectMocks
	private CourseService courseService;

	@Test
	public void givenCourse_whenCreate_thenCourseIsCreating() {
		Course course = buildCourse();

		courseService.create(course);

		verify(courseDao).create(course);
	}

	@Test
	public void givenNameIsNull_whenCreate_thenCourseIsNotCreating() {
		Course course = buildCourse();
		course.setName(null);

		courseService.create(course);

		verify(courseDao, never()).create(course);
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenCourseIsNotCreating() {
		Course course = buildCourse();
		course.setName("");

		courseService.create(course);

		verify(courseDao, never()).create(course);
	}

	@Test
	public void givenRoomsIsEmpty_whenCreate_thenCourseIsNotCreating() {
		Course course = buildCourse();
		course.setRooms(new HashSet<>());

		courseService.create(course);

		verify(courseDao, never()).create(course);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Course expected = buildCourse();
		when(courseDao.findById(1L)).thenReturn(expected);

		Course actual = courseService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Course> expected = Arrays.asList(buildCourse());
		when(courseDao.getAll()).thenReturn(expected);

		List<Course> actual = courseService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenCourse_whenUpdate_thenCourseIsUpdating() {
		Course course = buildCourse();

		courseService.update(course);

		verify(courseDao).update(course);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenCourseIsDeleting() {
		when(courseDao.findById(1L)).thenReturn(buildCourse());

		courseService.deleteById(1L);

		verify(courseDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenCourseIsNotDeleting() {
		when(courseDao.findById(1L)).thenThrow(EmptyResultDataAccessException.class);

		courseService.deleteById(1L);

		verify(courseDao, never()).deleteById(1L);
	}

	private Course buildCourse() {
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
