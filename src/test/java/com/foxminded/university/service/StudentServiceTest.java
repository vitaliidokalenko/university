package com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.exception.GroupOverflowException;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

	private static final int GROUP_SIZE = 2;

	@Mock
	private StudentDao studentDao;

	@InjectMocks
	private StudentService studentService;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(studentService, "groupSize", GROUP_SIZE);
	}

	@Test
	public void givenStudent_whenCreate_thenStudentIsCreating() {
		Student student = buildStudent();

		studentService.create(student);

		verify(studentDao).create(student);
	}

	@Test
	public void givenGenderIsNull_whenCreate_thenThrowException() {
		Student student = buildStudent();
		student.setGender(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> studentService.create(student));
		assertEquals("Empty student gender", exception.getMessage());
	}

	@Test
	public void givenNameIsNull_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Student student = buildStudent();
		student.setName(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> studentService.create(student));
		assertEquals("Empty student name", exception.getMessage());
	}

	@Test
	public void givenSurnameIsNull_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Student student = buildStudent();
		student.setSurname(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> studentService.create(student));
		assertEquals("Empty student surname", exception.getMessage());
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Student student = buildStudent();
		student.setName("");

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> studentService.create(student));
		assertEquals("Empty student name", exception.getMessage());
	}

	@Test
	public void givenSurnameIsEmpty_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Student student = buildStudent();
		student.setSurname("");

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> studentService.create(student));
		assertEquals("Empty student surname", exception.getMessage());
	}

	@Test
	public void givenGroupSizeIsNotEnuogh_whenCreate_thenGroupOverflowExceptionThrown() {
		Student student = buildStudent();
		when(studentDao.getByGroup(student.getGroup()))
				.thenReturn(List.of(new Student("Serhii", "Gerega"), new Student("Anatoly", "Soprano")));

		Exception exception = assertThrows(GroupOverflowException.class,
				() -> studentService.create(student));
		assertEquals(format("The group %s is overflow (size = %d)",
				student.getGroup().getName(),
				GROUP_SIZE), exception.getMessage());
	}

	@Test
	public void givenId_whenFindById_thenGetRightStudent() {
		Optional<Student> expected = Optional.of(buildStudent());
		when(studentDao.findById(1L)).thenReturn(expected);

		Optional<Student> actual = studentService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfStudents() {
		List<Student> expected = List.of(buildStudent());
		when(studentDao.getAll()).thenReturn(expected);

		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenStudent_whenUpdate_thenStudentIsUpdating() {
		Student student = buildStudent();

		studentService.update(student);

		verify(studentDao).update(student);
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
		when(studentDao.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		Page<Student> actual = studentService.getAllPage(PageRequest.of(0, 1));

		assertEquals(expected, actual);
	}

	private Student buildStudent() {
		Course course1 = new Course("Art");
		course1.setId(1L);
		Course course2 = new Course("Law");
		course2.setId(2L);
		Group group = new Group("AA-11");
		group.setId(1L);
		Student student = new Student("Homer", "Simpson");
		student.setId(1L);
		student.setCourses(Set.of(course1, course2));
		student.setGender(Gender.MALE);
		student.setGroup(group);
		return student;
	}
}
