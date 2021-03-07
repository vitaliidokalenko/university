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
				.setControllerAdvice(new ExceptionHandlingController())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightListOfStudents() throws Exception {
		List<Student> expected = Arrays.asList(buildStudent());
		when(studentService.getAll()).thenReturn(expected);

		mockMvc.perform(get("/students"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/students"))
				.andExpect(model().attribute("students", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightStudent() throws Exception {
		Optional<Student> expected = Optional.of(buildStudent());
		when(studentService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/students/1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/student"))
				.andExpect(model().attribute("student", expected.get()));
	}

	@Test
	public void givenStudentIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(studentService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/students/1"))
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
