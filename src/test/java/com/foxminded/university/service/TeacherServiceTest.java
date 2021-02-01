package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
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
	public void givenGenderIsNull_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = buildTeacher();
		teacher.setGender(null);

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = buildTeacher();
		teacher.setName("");

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}

	@Test
	public void givenSurnameIsEmpty_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = buildTeacher();
		teacher.setSurname("");

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}

	@Test
	public void givenNameIsNull_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = buildTeacher();
		teacher.setName(null);

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}

	@Test
	public void givenSurnameIsNull_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = buildTeacher();
		teacher.setSurname(null);

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
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
	public void givenEntityIsNotPresent_whenDeleteById_thenTeacherIsNotDeleting() {
		when(teacherDao.findById(1L)).thenReturn(Optional.empty());

		teacherService.deleteById(1L);

		verify(teacherDao, never()).deleteById(1L);
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
