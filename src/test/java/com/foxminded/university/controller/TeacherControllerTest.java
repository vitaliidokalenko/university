package com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.controller.exception.ControllerExceptionHandler;
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
				.setControllerAdvice(new ControllerExceptionHandler())
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightTeachersPage() throws Exception {
		Page<Teacher> expected = new PageImpl<>(Arrays.asList(buildTeacher()));
		when(teacherService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/teachers").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("teacher/teachers"))
				.andExpect(model().attribute("teachersPage", expected))
				.andExpect(model().attribute("numbers", IntStream.rangeClosed(1, expected.getTotalPages()).toArray()));
	}

	@Test
	public void givenId_whenFindById_thenGetRightTeacher() throws Exception {
		Optional<Teacher> expected = Optional.of(buildTeacher());
		when(teacherService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/teachers/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("teacher/teacher"))
				.andExpect(model().attribute("teacher", expected.get()));
	}

	@Test
	public void givenTeacherIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(teacherService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/teachers/{id}", 1))
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
