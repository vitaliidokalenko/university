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
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

	@Mock
	private TeacherDao teacherDao;
	@Mock
	private CourseDao courseDao;

	@InjectMocks
	private TeacherService teacherService;

	@Test
	public void givenTeacher_whenCreate_thenTeacherIsCreating() {
		Teacher teacher = new Teacher("Homer", "Simpson");

		teacherService.create(teacher);

		verify(teacherDao).create(teacher);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Teacher expected = new Teacher("Homer", "Simpson");
		Long id = 1L;
		expected.setId(id);
		when(teacherDao.findById(id)).thenReturn(expected);

		Teacher actual = teacherService.findById(id);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Teacher> expected = Arrays.asList(new Teacher("Homer", "Simpson"));
		when(teacherDao.getAll()).thenReturn(expected);

		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacher_whenUpdate_thenTeacherIsUpdating() {
		Teacher teacher = new Teacher("Homer", "Simpson");

		teacherService.update(teacher);

		verify(teacherDao).update(teacher);
	}

	@Test
	public void givenId_whenDeleteById_thenTeacherIsDeleting() {
		teacherService.deleteById(1L);

		verify(teacherDao).deleteById(1L);
	}

	@Test
	public void givenCourse_whenAddCourseById_thenDataIsUpdating() {
		Teacher teacher = new Teacher("Homer", "Simpson");
		Long id = 1L;
		teacher.setCourses(new HashSet<>(Arrays.asList(new Course("Art"))));
		Course course = new Course("Law");
		when(teacherDao.findById(id)).thenReturn(teacher);
		when(courseDao.findById(id)).thenReturn(course);

		teacherService.addCourseById(id, id);

		verify(teacherDao).update(teacher);
	}

	@Test
	public void givenCourse_whenRemoveCourseById_thenDataIsUpdating() {
		Teacher teacher = new Teacher("Homer", "Simpson");
		Long id = 1L;
		Course course = new Course("Art");
		teacher.setCourses(new HashSet<>(Arrays.asList(course)));
		when(teacherDao.findById(id)).thenReturn(teacher);
		when(courseDao.findById(id)).thenReturn(course);

		teacherService.removeCourseById(id, id);

		verify(teacherDao).update(teacher);
	}

	@Test
	public void givenId_whenGetTeachersByCourseId_thenGetRightData() {
		List<Teacher> expected = Arrays.asList(new Teacher("Homer", "Simpson"));
		when(teacherDao.getTeachersByCourseId(anyLong())).thenReturn(expected);

		List<Teacher> actual = teacherService.getTeachersByCourseId(anyLong());

		assertEquals(expected, actual);
	}
}
