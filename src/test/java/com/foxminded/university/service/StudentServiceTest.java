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

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.exception.GroupOverflowException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

	@Mock
	private StudentDao studentDao;
	@Mock
	private UniversityConfigProperties properties;

	@InjectMocks
	private StudentService studentService;

	@Test
	public void givenStudent_whenCreate_thenStudentIsCreating() {
		Student student = buildStudent();
		int maxGroupSize = 2;
		when(properties.getMaxGroupSize()).thenReturn(maxGroupSize);

		studentService.create(student);

		verify(studentDao).save(student);
	}

	@Test
	public void givenGroupSizeIsNotEnuogh_whenCreate_thenGroupOverflowExceptionThrown() {
		int maxGroupSize = 2;
		Student student = buildStudent();
		when(studentDao.getByGroup(student.getGroup()))
				.thenReturn(List.of(Student.builder().name("Anna").surname("Maria").build(),
						Student.builder().name("Anatoly").surname("Deineka").build()));
		when(properties.getMaxGroupSize()).thenReturn(maxGroupSize);

		Exception exception = assertThrows(GroupOverflowException.class,
				() -> studentService.create(student));
		assertEquals(format("The group %s is overflow (size = %d)",
				student.getGroup().getName(),
				maxGroupSize), exception.getMessage());
	}

	@Test
	public void givenId_whenFindById_thenGetRightStudent() {
		Student expected = buildStudent();
		when(studentDao.findById(1L)).thenReturn(Optional.of(expected));

		Student actual = studentService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfStudents() {
		List<Student> expected = List.of(buildStudent());
		when(studentDao.findAll()).thenReturn(expected);

		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenStudent_whenUpdate_thenStudentIsUpdating() {
		Student student = buildStudent();
		int maxGroupSize = 2;
		when(studentDao.findById(student.getId())).thenReturn(Optional.of(student));
		when(properties.getMaxGroupSize()).thenReturn(maxGroupSize);

		studentService.update(student);

		verify(studentDao).save(student);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenStudentIsDeleting() {
		Student student = buildStudent();
		when(studentDao.findById(1L)).thenReturn(Optional.of(student));

		studentService.deleteById(1L);

		verify(studentDao).delete(student);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenNotFoundEntityExceptionThrown() {
		when(studentDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> studentService.deleteById(1L));
		assertEquals("Cannot find student by id: 1", exception.getMessage());
	}

	@Test
	public void whenGetAllPage_thenGetRightStudents() {
		Page<Student> expected = new PageImpl<>(List.of(buildStudent()));
		when(studentDao.findAll(PageRequest.of(0, 1))).thenReturn(expected);

		Page<Student> actual = studentService.getAllPage(PageRequest.of(0, 1));

		assertEquals(expected, actual);
	}

	@Test
	public void givenEntityIsNotPresent_whenFindById_thenNotFoundEntityExceptionThrown() {
		when(studentDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> studentService.findById(1L));
		assertEquals("Cannot find student by id: 1", exception.getMessage());
	}

	@Test
	public void givenEntityIsNotPresent_whenUpdate_thenNotFoundEntityExceptionThrown() {
		Student student = buildStudent();
		when(studentDao.findById(student.getId())).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> studentService.update(student));
		assertEquals("Cannot find student by id: 1", exception.getMessage());
	}

	private Student buildStudent() {
		return Student.builder()
				.id(1L)
				.name("Homer")
				.surname("Simpson")
				.courses(Set.of(Course.builder().id(1L).name("Art").build(),
						Course.builder().id(2L).name("Law").build()))
				.gender(Gender.MALE)
				.group(Group.builder().id(1L).name("AA-11").build())
				.build();
	}
}
