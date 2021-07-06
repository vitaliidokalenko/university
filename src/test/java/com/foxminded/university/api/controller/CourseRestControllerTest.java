package com.foxminded.university.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.CourseService;

@RunWith(SpringRunner.class)
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
				.andExpect(jsonPath("$.content[0].id", is(1)))
				.andExpect(jsonPath("$.content[0].name", is("Art")))
				.andExpect(status().isOk());
	}

	@Test
	public void givenId_whenGetById_thenGetRightCourse() throws Exception {
		Course expected = buildCourse();
		when(courseService.findById(1L)).thenReturn(Optional.of(expected));

		mockMvc.perform(get("/api/v1/courses/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Art")))
				.andExpect(status().isOk());
	}

	@Test
	public void givenCourseIsNotPresent_whenGetById_thenStatusIsBadRequest() throws Exception {
		when(courseService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/v1/courses/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenNewCourse_whenCreate_thenCourseIsCreated() throws Exception {
		Course course = buildCourse();
		course.setId(null);

		mockMvc.perform(post("/api/v1/courses")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(course)))
				.andExpect(status().isCreated());

		verify(courseService).create(course);
	}

	@Test
	public void givenCourse_whenUpdate_thenCourseIsUpdated() throws Exception {
		Course course = buildCourse();

		mockMvc.perform(put("/api/v1/courses/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(course)))
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
