package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
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
	@Mock
	private GroupDao groupDao;
	@Mock
	private CourseDao courseDao;

	@InjectMocks
	private StudentService studentService;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(studentService, "groupSize", GROUP_SIZE);
	}

	@Test
	public void givenStudent_whenCreate_thenStudentIsCreating() {
		Student student = getStandardStudent();

		studentService.create(student);

		verify(studentDao).create(student);
	}

	@Test
	public void givenGenderIsNull_whenCreate_thenStudentIsNotCreating() {
		Student student = getStandardStudent();
		student.setGender(null);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenNameIsNull_whenCreate_thenStudentIsNotCreating() {
		Student student = getStandardStudent();
		student.setName(null);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenSurnameIsNull_whenCreate_thenStudentIsNotCreating() {
		Student student = getStandardStudent();
		student.setSurname(null);

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenStudentIsNotCreating() {
		Student student = getStandardStudent();
		student.setName("");

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenSurnameIsEmpty_whenCreate_thenStudentIsNotCreating() {
		Student student = getStandardStudent();
		student.setSurname("");

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenGroupSizeIsNotEnuogh_whenCreate_thenStudentIsNotCreating() {
		Student student = getStandardStudent();
		when(studentDao.getStudentsByGroup(student.getGroup()))
				.thenReturn(Arrays.asList(new Student("Serhii", "Gerega"), new Student("Anatoly", "Soprano")));

		studentService.create(student);

		verify(studentDao, never()).create(student);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Student expected = getStandardStudent();
		when(studentDao.findById(1L)).thenReturn(expected);

		Student actual = studentService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Student> expected = Arrays.asList(getStandardStudent());
		when(studentDao.getAll()).thenReturn(expected);

		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenStudent_whenUpdate_thenStudentIsUpdating() {
		Student student = getStandardStudent();

		studentService.update(student);

		verify(studentDao).update(student);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenStudentIsDeleting() {
		when(studentDao.findById(1L)).thenReturn(getStandardStudent());

		studentService.deleteById(1L);

		verify(studentDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenStudentIsNotDeleting() {
		when(studentDao.findById(1L)).thenThrow(EmptyResultDataAccessException.class);

		studentService.deleteById(1L);

		verify(studentDao, never()).deleteById(1L);
	}

	@Test
	public void givenGroup_whenSetGroupById_thenDataIsUpdating() {
		Student student = getStandardStudent();
		Group group = new Group("BB-22");
		when(studentDao.findById(1L)).thenReturn(student);
		when(groupDao.findById(anyLong())).thenReturn(group);

		studentService.setGroupById(1L, anyLong());

		verify(studentDao).update(student);
	}

	@Test
	public void givenGroup_whenRemoveGroupById_thenDataIsUpdating() {
		Student student = getStandardStudent();
		when(studentDao.findById(1L)).thenReturn(student);

		studentService.removeGroupById(1L);

		assertNull(student.getGroup());
		verify(studentDao).update(student);
	}

	@Test
	public void givenCourse_whenAddCourseById_thenDataIsUpdating() {
		Student student = getStandardStudent();
		Course course = new Course("Philosophy");
		when(studentDao.findById(1L)).thenReturn(student);
		when(courseDao.findById(anyLong())).thenReturn(course);

		studentService.addCourseById(1L, anyLong());

		assertTrue(student.getCourses().contains(course));
		verify(studentDao).update(student);
	}

	@Test
	public void givenCourse_whenRemoveCourseById_thenDataIsUpdating() {
		Student student = getStandardStudent();
		Course course = new Course("Art");
		course.setId(1L);
		student.setCourses(new HashSet<>(Arrays.asList(course)));
		when(studentDao.findById(1L)).thenReturn(student);
		when(courseDao.findById(anyLong())).thenReturn(course);

		studentService.removeCourseById(1L, anyLong());

		assertFalse(student.getCourses().contains(course));
		verify(studentDao).update(student);
	}

	@Test
	public void givenId_whenGetStudentsByGroup_thenGetRightData() {
		List<Student> expected = Arrays.asList(getStandardStudent());
		Group group = new Group("AA-11");
		when(studentDao.getStudentsByGroup(group)).thenReturn(expected);

		List<Student> actual = studentService.getStudentsByGroup(group);

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetStudentsByCourseId_thenGetRightData() {
		List<Student> expected = Arrays.asList(getStandardStudent());
		when(studentDao.getStudentsByCourseId(anyLong())).thenReturn(expected);

		List<Student> actual = studentService.getStudentsByCourseId(anyLong());

		assertEquals(expected, actual);
	}

	private Student getStandardStudent() {
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
