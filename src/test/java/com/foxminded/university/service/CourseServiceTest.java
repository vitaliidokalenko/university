package com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.IncompleteEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

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
	public void givenNameIsNull_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Course course = buildCourse();
		course.setName(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> courseService.create(course));
		assertEquals("Empty course name", exception.getMessage());
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Course course = buildCourse();
		course.setName("");

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> courseService.create(course));
		assertEquals("Empty course name", exception.getMessage());
	}

	@Test
	public void givenRoomsIsEmpty_whenCreate_thenIncompleteEntityExceptionThrown() {
		Course course = buildCourse();
		course.setRooms(new HashSet<>());

		Exception exception = assertThrows(IncompleteEntityException.class, () -> courseService.create(course));
		assertEquals(format("No rooms assigned to the course: %s", course.getName()), exception.getMessage());
	}

	@Test
	public void givenId_whenFindById_thenGetRightCourse() {
		Optional<Course> expected = Optional.of(buildCourse());
		when(courseDao.findById(1L)).thenReturn(expected);

		Optional<Course> actual = courseService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfCourses() {
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
		when(courseDao.findById(1L)).thenReturn(Optional.of(buildCourse()));

		courseService.deleteById(1L);

		verify(courseDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenNotFoundEntityExceptionThrown() {
		when(courseDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> courseService.deleteById(1L));
		assertEquals("Cannot find course by id: 1", exception.getMessage());
	}

	@Test
	public void givenNameIsNotUnique_whenCreate_thenNotUniqueNameExceptionThrown() {
		Course actual = buildCourse();
		Course retrieved = buildCourse();
		retrieved.setId(2L);
		when(courseDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		Exception exception = assertThrows(NotUniqueNameException.class, () -> courseService.create(actual));
		assertEquals(format("The course with name %s already exists", actual.getName()), exception.getMessage());
	}

	@Test
	public void givenNameIsUnique_whenCreate_thenCourseIsCreating() {
		Course actual = buildCourse();
		when(courseDao.findByName(actual.getName())).thenReturn(Optional.empty());

		courseService.create(actual);

		verify(courseDao).create(actual);
	}

	@Test
	public void givenNameIsUnique_whenUpdate_thenCourseIsUpdating() {
		Course actual = buildCourse();
		Course retrieved = buildCourse();
		when(courseDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		courseService.update(actual);

		verify(courseDao).update(actual);
	}

	@Test
	public void givenNameIsNotUnique_whenUpdate_thenNotUniqueNameExceptionThrown() {
		Course actual = buildCourse();
		Course retrieved = buildCourse();
		retrieved.setId(2L);
		when(courseDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		Exception exception = assertThrows(NotUniqueNameException.class, () -> courseService.update(actual));
		assertEquals(format("The course with name %s already exists", actual.getName()), exception.getMessage());
	}

	@Test
	public void whenGetAllPage_thenGetRightCourses() {
		Page<Course> expected = new PageImpl<>(Arrays.asList(buildCourse()));
		when(courseDao.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		Page<Course> actual = courseService.getAllPage(PageRequest.of(0, 1));

		assertEquals(expected, actual);
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
