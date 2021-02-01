package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
		"group.size=2"
})
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
	public void givenGenderIsNull_whenCreate_thenStudentIsNotCreating() {
		Student student = buildStudent();
		student.setGender(null);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenNameIsNull_whenCreate_thenStudentIsNotCreating() {
		Student student = buildStudent();
		student.setName(null);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenSurnameIsNull_whenCreate_thenStudentIsNotCreating() {
		Student student = buildStudent();
		student.setSurname(null);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenStudentIsNotCreating() {
		Student student = buildStudent();
		student.setName("");

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenSurnameIsEmpty_whenCreate_thenStudentIsNotCreating() {
		Student student = buildStudent();
		student.setSurname("");

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenGroupSizeIsNotEnuogh_whenCreate_thenStudentIsNotCreating() {
		Student student = buildStudent();
		when(studentDao.getByGroup(student.getGroup()))
				.thenReturn(Arrays.asList(new Student("Serhii", "Gerega"), new Student("Anatoly", "Soprano")));

		studentService.create(student);

		verify(studentDao, never()).create(student);
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
		List<Student> expected = Arrays.asList(buildStudent());
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
		when(studentDao.findById(1L)).thenReturn(Optional.of(buildStudent()));

		studentService.deleteById(1L);

		verify(studentDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenStudentIsNotDeleting() {
		when(studentDao.findById(1L)).thenReturn(Optional.empty());

		studentService.deleteById(1L);

		verify(studentDao, never()).deleteById(1L);
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
		student.setCourses(new HashSet<>(Arrays.asList(course1, course2)));
		student.setGender(Gender.MALE);
		student.setGroup(group);
		return student;
	}
}
