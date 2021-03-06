package com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

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

		verify(courseDao).save(course);
	}

	@Test
	public void givenId_whenFindById_thenGetRightCourse() {
		Course expected = buildCourse();
		when(courseDao.findById(1L)).thenReturn(Optional.of(expected));

		Course actual = courseService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfCourses() {
		List<Course> expected = List.of(buildCourse());
		when(courseDao.findAll()).thenReturn(expected);

		List<Course> actual = courseService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenCourse_whenUpdate_thenCourseIsUpdating() {
		Course course = buildCourse();
		when(courseDao.findById(course.getId())).thenReturn(Optional.of(course));

		courseService.update(course);

		verify(courseDao).save(course);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenCourseIsDeleting() {
		Course course = buildCourse();
		when(courseDao.findById(1L)).thenReturn(Optional.of(course));

		courseService.deleteById(1L);

		verify(courseDao).delete(course);
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

		verify(courseDao).save(actual);
	}

	@Test
	public void givenNameIsUnique_whenUpdate_thenCourseIsUpdating() {
		Course actual = buildCourse();
		Course retrieved = buildCourse();
		when(courseDao.findById(actual.getId())).thenReturn(Optional.of(actual));
		when(courseDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		courseService.update(actual);

		verify(courseDao).save(actual);
	}

	@Test
	public void givenNameIsNotUnique_whenUpdate_thenNotUniqueNameExceptionThrown() {
		Course actual = buildCourse();
		Course retrieved = buildCourse();
		retrieved.setId(2L);
		when(courseDao.findById(actual.getId())).thenReturn(Optional.of(actual));
		when(courseDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		Exception exception = assertThrows(NotUniqueNameException.class, () -> courseService.update(actual));
		assertEquals(format("The course with name %s already exists", actual.getName()), exception.getMessage());
	}

	@Test
	public void whenGetAllPage_thenGetRightCourses() {
		Page<Course> expected = new PageImpl<>(List.of(buildCourse()));
		when(courseDao.findAll(PageRequest.of(0, 1))).thenReturn(expected);

		Page<Course> actual = courseService.getAllPage(PageRequest.of(0, 1));

		assertEquals(expected, actual);
	}

	@Test
	public void givenEntityIsNotPresent_whenFindById_thenNotFoundEntityExceptionThrown() {
		when(courseDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> courseService.findById(1L));
		assertEquals("Cannot find course by id: 1", exception.getMessage());
	}

	@Test
	public void givenEntityIsNotPresent_whenUpdate_thenNotFoundEntityExceptionThrown() {
		Course course = buildCourse();
		when(courseDao.findById(course.getId())).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> courseService.update(course));
		assertEquals("Cannot find course by id: 1", exception.getMessage());
	}

	private Course buildCourse() {
		return Course.builder()
				.id(1L)
				.name("Art")
				.rooms(Set.of(Room.builder().id(1L).name("111").build(), Room.builder().id(2L).name("222").build()))
				.build();
	}
}
