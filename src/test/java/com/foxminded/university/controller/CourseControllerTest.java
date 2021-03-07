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
import com.foxminded.university.model.Room;
import com.foxminded.university.service.CourseService;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

	@Mock
	private CourseService courseService;
	@InjectMocks
	private CourseController courseController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUpp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(courseController)
				.setControllerAdvice(new ExceptionHandlingController())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightListOfCourses() throws Exception {
		List<Course> expected = Arrays.asList(buildCourse());
		when(courseService.getAll()).thenReturn(expected);

		mockMvc.perform(get("/courses"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("course/courses"))
				.andExpect(model().attribute("courses", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightCourse() throws Exception {
		Optional<Course> expected = Optional.of(buildCourse());
		when(courseService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/courses/1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("course/course"))
				.andExpect(model().attribute("course", expected.get()));
	}

	@Test
	public void givenCourseIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(courseService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/courses/1"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception", "NotFoundEntityException"))
				.andExpect(model().attribute("message", "Cannot find course by id: 1"))
				.andExpect(forwardedUrl("error"));
	}

	private Course buildCourse() {
		Room room1 = new Room("111");
		room1.setId(1L);
		Room room2 = new Room("222");
		room2.setId(2L);
		Course course = new Course("Art");
		course.setId(1L);
		course.setRooms(new HashSet<>(Arrays.asList(room1, room2)));
		return course;
	}
}
