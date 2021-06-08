package com.foxminded.university.service;

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

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

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

		verify(teacherDao).save(teacher);
	}

	@Test
	public void givenGenderIsNull_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Teacher teacher = buildTeacher();
		teacher.setGender(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals("Empty teacher gender", exception.getMessage());
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Teacher teacher = buildTeacher();
		teacher.setName("");

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals("Empty teacher name", exception.getMessage());
	}

	@Test
	public void givenSurnameIsEmpty_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Teacher teacher = buildTeacher();
		teacher.setSurname("");

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals("Empty teacher surname", exception.getMessage());
	}

	@Test
	public void givenNameIsNull_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Teacher teacher = buildTeacher();
		teacher.setName(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals("Empty teacher name", exception.getMessage());
	}

	@Test
	public void givenSurnameIsNull_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Teacher teacher = buildTeacher();
		teacher.setSurname(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> teacherService.create(teacher));
		assertEquals("Empty teacher surname", exception.getMessage());
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
		List<Teacher> expected = List.of(buildTeacher());
		when(teacherDao.findAll()).thenReturn(expected);

		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacher_whenUpdate_thenTeacherIsUpdating() {
		Teacher teacher = buildTeacher();

		teacherService.update(teacher);

		verify(teacherDao).save(teacher);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenTeacherIsDeleting() {
		Teacher teacher = buildTeacher();
		when(teacherDao.findById(1L)).thenReturn(Optional.of(teacher));

		teacherService.deleteById(1L);

		verify(teacherDao).delete(teacher);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenNotFoundEntityExceptionThrown() {
		when(teacherDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> teacherService.deleteById(1L));
		assertEquals("Cannot find teacher by id: 1", exception.getMessage());
	}

	@Test
	public void whenGetAllPage_thenGetRightTeachers() {
		Page<Teacher> expected = new PageImpl<>(List.of(buildTeacher()));
		when(teacherDao.findAll(PageRequest.of(0, 1))).thenReturn(expected);

		Page<Teacher> actual = teacherService.getAllPage(PageRequest.of(0, 1));

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacher_whenGetSubstituteTeachers_thenGetRightSetOfTeachers() {
		Teacher teacher = buildTeacher();
		Teacher substitute = buildTeacher();
		substitute.setId(2L);
		Set<Teacher> expected = Set.of(substitute);
		when(teacherDao.getByCoursesId(1L)).thenReturn(List.of(teacher));
		when(teacherDao.getByCoursesId(2L)).thenReturn(List.of(substitute));

		Set<Teacher> actual = teacherService.getSubstituteTeachers(teacher);

		assertEquals(expected, actual);
	}

	private Teacher buildTeacher() {
		return Teacher.builder()
				.id(1L)
				.name("Homer")
				.surname("Simpson")
				.courses(Set.of(Course.builder().id(1L).name("Art").build(),
						Course.builder().id(2L).name("Law").build()))
				.gender(Gender.MALE)
				.build();
	}
}
