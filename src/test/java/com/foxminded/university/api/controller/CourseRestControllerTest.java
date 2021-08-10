package com.foxminded.university.api.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@WebMvcTest(CourseRestController.class)
public class CourseRestControllerTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CourseService courseService;

	@Test
	public void whenGetAll_thenGetRightCoursesPage() throws Exception {
		Page<Course> expected = new PageImpl<>(List.of(buildCourse()));
		when(courseService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/api/v1/courses").param("page", "0")
				.param("size", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expected)))
				.andExpect(status().isOk());
	}

	@Test
	public void givenId_whenGetById_thenGetRightCourse() throws Exception {
		Course expected = buildCourse();
		when(courseService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/api/v1/courses/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expected)))
				.andExpect(status().isOk());
	}

	@Test
	public void givenCourseIsNotPresent_whenGetById_thenStatusIsNotFound() throws Exception {
		when(courseService.findById(1L)).thenThrow(new NotFoundEntityException());

		mockMvc.perform(get("/api/v1/courses/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void givenNewCourse_whenCreate_thenCourseIsCreated() throws Exception {
		Course course = buildCourse();

		mockMvc.perform(post("/api/v1/courses")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(course)))
				.andExpect(header().string("Location", containsString("/api/v1/courses/1")))
				.andExpect(redirectedUrlPattern("http://*/api/v1/courses/1"))
				.andExpect(status().isCreated());

		verify(courseService).create(course);
	}

	@Test
	public void givenCourse_whenUpdate_thenCourseIsUpdated() throws Exception {
		Course course = buildCourse();

		mockMvc.perform(put("/api/v1/courses/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(course)))
				.andExpect(status().isOk());

		verify(courseService).update(course);
	}

	@Test
	public void givenId_whenDelete_thenCourseIsDeleted() throws Exception {
		mockMvc.perform(delete("/api/v1/courses/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(courseService).deleteById(1L);
	}

	private Course buildCourse() {
		return Course.builder()
				.id(1L)
				.name("Art")
				.rooms(Set.of(Room.builder().id(1L).name("111").capacity(30).build(),
						Room.builder().id(2L).name("222").capacity(30).build()))
				.build();
	}
}
