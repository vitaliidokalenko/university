package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

	@Mock
	private StudentDao studentDao;
	@Mock
	private GroupDao groupDao;
	@Mock
	private CourseDao courseDao;

	@InjectMocks
	private StudentService studentService;

	@Test
	public void givenStudent_whenCreate_thenStudentIsCreating() {
		Student student = new Student("Homer", "Simpson");

		studentService.create(student);

		verify(studentDao).create(student);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Student expected = new Student("Homer", "Simpson");
		Long id = 1L;
		expected.setId(id);
		when(studentDao.findById(id)).thenReturn(expected);

		Student actual = studentService.findById(id);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Student> expected = Arrays.asList(new Student("Homer", "Simpson"));
		when(studentDao.getAll()).thenReturn(expected);

		List<Student> actual = studentService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenStudent_whenUpdate_thenStudentIsUpdating() {
		Student student = new Student("Homer", "Simpson");

		studentService.update(student);

		verify(studentDao).update(student);
	}

	@Test
	public void givenId_whenDeleteById_thenStudentIsDeleting() {
		studentService.deleteById(1L);

		verify(studentDao).deleteById(1L);
	}

	@Test
	public void givenGroup_whenSetGroupById_thenDataIsUpdating() {
		Student student = new Student("Homer", "Simpson");
		Long id = 1L;
		Group group = new Group("AA-11");
		when(studentDao.findById(id)).thenReturn(student);
		when(groupDao.findById(id)).thenReturn(group);

		studentService.setGroupById(id, id);

		verify(studentDao).update(student);
	}

	@Test
	public void givenGroup_whenRemoveGroupById_thenDataIsUpdating() {
		Student student = new Student("Homer", "Simpson");
		Long id = 1L;
		Group group = new Group("AA-11");
		student.setGroup(group);
		when(studentDao.findById(id)).thenReturn(student);

		studentService.removeGroupById(id);

		verify(studentDao).update(student);
	}

	@Test
	public void givenCourse_whenAddCourseById_thenDataIsUpdating() {
		Student student = new Student("Homer", "Simpson");
		Long id = 1L;
		student.setCourses(new HashSet<>(Arrays.asList(new Course("Art"))));
		Course course = new Course("Law");
		when(studentDao.findById(id)).thenReturn(student);
		when(courseDao.findById(id)).thenReturn(course);

		studentService.addCourseById(id, id);

		verify(studentDao).update(student);
	}

	@Test
	public void givenCourse_whenRemoveCourseById_thenDataIsUpdating() {
		Student student = new Student("Homer", "Simpson");
		Long id = 1L;
		Course course = new Course("Art");
		student.setCourses(new HashSet<>(Arrays.asList(course)));
		when(studentDao.findById(id)).thenReturn(student);
		when(courseDao.findById(id)).thenReturn(course);

		studentService.removeCourseById(id, id);

		verify(studentDao).update(student);
	}

	@Test
	public void givenId_whenGetStudentsByGroup_thenGetRightData() {
		List<Student> expected = Arrays.asList(new Student("Homer", "Simpson"));
		Group group = new Group("AA-11");
		when(studentDao.getStudentsByGroup(group)).thenReturn(expected);

		List<Student> actual = studentService.getStudentsByGroup(group);

		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenGetStudentsByCourseId_thenGetRightData() {
		List<Student> expected = Arrays.asList(new Student("Homer", "Simpson"));
		when(studentDao.getStudentsByCourseId(anyLong())).thenReturn(expected);

		List<Student> actual = studentService.getStudentsByCourseId(anyLong());

		assertEquals(expected, actual);
	}
}
