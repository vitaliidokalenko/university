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
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

	@Mock
	private TeacherDao teacherDao;

	@InjectMocks
	private TeacherService teacherService;

	@Test
	public void givenTeacher_whenCreate_thenTeacherIsCreating() {
		Teacher teacher = buildTeacher();

		teacherService.create(teacher);

		verify(teacherDao).create(teacher);
	}

	@Test
	public void givenGenderIsNull_whenCreate_thenThrowException() {
		Teacher teacher = buildTeacher();
		teacher.setGender(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals(format("Gender of the teacher %s %s is absent", teacher.getName(), teacher.getSurname()),
				exception.getMessage());
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenThrowException() {
		Teacher teacher = buildTeacher();
		teacher.setName("");

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals("The name of the teacher is empty", exception.getMessage());
	}

	@Test
	public void givenSurnameIsEmpty_whenCreate_thenThrowException() {
		Teacher teacher = buildTeacher();
		teacher.setSurname("");

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals("The surname of the teacher is empty", exception.getMessage());
	}

	@Test
	public void givenNameIsNull_whenCreate_thenThrowException() {
		Teacher teacher = buildTeacher();
		teacher.setName(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals("The name of the teacher is absent", exception.getMessage());
	}

	@Test
	public void givenSurnameIsNull_whenCreate_thenThrowException() {
		Teacher teacher = buildTeacher();
		teacher.setSurname(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals("The surname of the teacher is absent", exception.getMessage());
	}

	@Test
	public void givenId_whenFindById_thenGetRightTeacher() {
		Optional<Teacher> expected = Optional.of(buildTeacher());
		when(teacherDao.findById(1L)).thenReturn(expected);

		Optional<Teacher> actual = teacherService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfTeachers() {
		List<Teacher> expected = Arrays.asList(buildTeacher());
		when(teacherDao.getAll()).thenReturn(expected);

		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacher_whenUpdate_thenTeacherIsUpdating() {
		Teacher teacher = buildTeacher();

		teacherService.update(teacher);

		verify(teacherDao).update(teacher);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenTeacherIsDeleting() {
		when(teacherDao.findById(1L)).thenReturn(Optional.of(buildTeacher()));

		teacherService.deleteById(1L);

		verify(teacherDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenThrowException() {
		when(teacherDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> teacherService.deleteById(1L));
		assertEquals("Cannot find teacher by id: 1", exception.getMessage());
	}

	private Teacher buildTeacher() {
		Course course1 = new Course("Art");
		course1.setId(1L);
		Course course2 = new Course("Law");
		course2.setId(2L);
		Teacher teacher = new Teacher("Homer", "Simpson");
		teacher.setId(1L);
		teacher.setCourses(new HashSet<>(Arrays.asList(course1, course2)));
		teacher.setGender(Gender.MALE);
		return teacher;
	}
}
