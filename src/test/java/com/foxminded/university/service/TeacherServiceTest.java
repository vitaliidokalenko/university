package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
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
import com.foxminded.university.model.Gender;
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
		Teacher teacher = getStandardTeacher();

		teacherService.create(teacher);

		verify(teacherDao).create(teacher);
	}

	@Test
	public void givenGenderIsNull_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = getStandardTeacher();
		teacher.setGender(null);

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = getStandardTeacher();
		teacher.setName("");

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}

	@Test
	public void givenSurnameIsEmpty_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = getStandardTeacher();
		teacher.setSurname("");

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}

	@Test
	public void givenNameIsNull_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = getStandardTeacher();
		teacher.setName(null);

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}

	@Test
	public void givenSurnameIsNull_whenCreate_thenTeacherIsNotCreating() {
		Teacher teacher = getStandardTeacher();
		teacher.setSurname(null);

		teacherService.create(teacher);

		verify(teacherDao, never()).create(teacher);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Teacher expected = getStandardTeacher();
		when(teacherDao.findById(1L)).thenReturn(expected);

		Teacher actual = teacherService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Teacher> expected = Arrays.asList(getStandardTeacher());
		when(teacherDao.getAll()).thenReturn(expected);

		List<Teacher> actual = teacherService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTeacher_whenUpdate_thenTeacherIsUpdating() {
		Teacher teacher = getStandardTeacher();

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
		Teacher teacher = getStandardTeacher();
		Course course = new Course("Philosophy");
		when(teacherDao.findById(1L)).thenReturn(teacher);
		when(courseDao.findById(anyLong())).thenReturn(course);

		teacherService.addCourseById(1L, anyLong());

		verify(teacherDao).update(teacher);
	}

	@Test
	public void givenCourse_whenRemoveCourseById_thenDataIsUpdating() {
		Teacher teacher = getStandardTeacher();
		Course course = new Course("Art");
		course.setId(1L);
		when(teacherDao.findById(1l)).thenReturn(teacher);
		when(courseDao.findById(anyLong())).thenReturn(course);

		teacherService.removeCourseById(1L, anyLong());

		verify(teacherDao).update(teacher);
	}

	@Test
	public void givenId_whenGetTeachersByCourseId_thenGetRightData() {
		List<Teacher> expected = Arrays.asList(getStandardTeacher());
		when(teacherDao.getTeachersByCourseId(anyLong())).thenReturn(expected);

		List<Teacher> actual = teacherService.getTeachersByCourseId(anyLong());

		assertEquals(expected, actual);
	}

	private Teacher getStandardTeacher() {
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
