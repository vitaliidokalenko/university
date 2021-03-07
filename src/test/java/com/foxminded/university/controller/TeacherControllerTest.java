package com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

	@Mock
	private TeacherService teacherService;
	@InjectMocks
	private TeacherController teacherController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUpp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
				.setControllerAdvice(new ExceptionHandlingController())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightListOfTeachers() throws Exception {
		List<Teacher> expected = Arrays.asList(buildTeacher());
		when(teacherService.getAll()).thenReturn(expected);

		mockMvc.perform(get("/teachers"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("teacher/teachers"))
				.andExpect(model().attribute("teachers", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightTeacher() throws Exception {
		Optional<Teacher> expected = Optional.of(buildTeacher());
		when(teacherService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/teachers/1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("teacher/teacher"))
				.andExpect(model().attribute("teacher", expected.get()));
	}

	@Test
	public void givenTeacherIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(teacherService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/teachers/1"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception", "NotFoundEntityException"))
				.andExpect(model().attribute("message", "Cannot find teacher by id: 1"))
				.andExpect(forwardedUrl("error"));
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
