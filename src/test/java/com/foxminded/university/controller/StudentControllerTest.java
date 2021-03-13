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
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

	@Mock
	private StudentService studentService;
	@InjectMocks
	private StudentController studentController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUpp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentController)
				.setControllerAdvice(new ControllerExceptionHandler())
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightStudentsPage() throws Exception {
		Page<Student> expected = new PageImpl<>(Arrays.asList(buildStudent()));
		when(studentService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/students").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/students"))
				.andExpect(model().attribute("studentsPage", expected))
				.andExpect(model().attribute("numbers", IntStream.rangeClosed(1, expected.getTotalPages()).toArray()));
	}

	@Test
	public void givenId_whenFindById_thenGetRightStudent() throws Exception {
		Optional<Student> expected = Optional.of(buildStudent());
		when(studentService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/students/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/student"))
				.andExpect(model().attribute("student", expected.get()));
	}

	@Test
	public void givenStudentIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(studentService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/students/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception", "NotFoundEntityException"))
				.andExpect(model().attribute("message", "Cannot find student by id: 1"))
				.andExpect(forwardedUrl("error"));
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
