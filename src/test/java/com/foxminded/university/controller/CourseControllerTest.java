package com.foxminded.university.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

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

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.RoomService;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

	@Mock
	private CourseService courseService;
	@Mock
	private RoomService roomService;
	@InjectMocks
	private CourseController courseController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(courseController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightCoursesPage() throws Exception {
		Page<Course> expected = new PageImpl<>(List.of(buildCourse()));
		when(courseService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/courses").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("course/courses"))
				.andExpect(model().attribute("coursesPage", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightCourse() throws Exception {
		Course expected = buildCourse();
		when(courseService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/courses/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("course/course"))
				.andExpect(model().attribute("course", expected));
	}

	@Test
	public void whenCreate_thenAddedNewCourseAttribute() throws Exception {
		when(roomService.getAll()).thenReturn(buildRooms());

		mockMvc.perform(get("/courses/new"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("rooms", roomService.getAll()))
				.andExpect(model().attribute("course", new Course()))
				.andExpect(forwardedUrl("course/create"));
	}

	@Test
	public void whenUpdate_thenAddedRightCourseAttribute() throws Exception {
		Course expected = buildCourse();
		when(courseService.findById(1L)).thenReturn(expected);
		when(roomService.getAll()).thenReturn(buildRooms());

		mockMvc.perform(get("/courses/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("rooms", roomService.getAll()))
				.andExpect(model().attribute("course", expected))
				.andExpect(forwardedUrl("course/edit"));
	}

	@Test
	public void givenNewCourse_whenSave_thenCourseIsCreating() throws Exception {
		Course course = Course.builder()
				.name("Art")
				.rooms(Set.of(Room.builder().id(1L).build()))
				.build();
		when(roomService.findById(1L)).thenReturn(Room.builder().id(1L).name("111").capacity(30).build());

		mockMvc.perform(post("/courses/save").flashAttr("course", course))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/courses"));

		verify(courseService).create(course);
	}

	@Test
	public void givenCourse_whenSave_thenCourseIsUpdating() throws Exception {
		Course course = Course.builder()
				.id(1L)
				.name("Art")
				.rooms(Set.of(Room.builder().id(1L).build()))
				.build();
		when(roomService.findById(1L)).thenReturn(Room.builder().id(1L).name("111").capacity(30).build());

		mockMvc.perform(post("/courses/save").flashAttr("course", course))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/courses"));

		verify(courseService).update(course);
	}

	@Test
	public void givenNotValidCourse_whenSave_thenForwardedEditView() throws Exception {
		Course course = Course.builder()
				.name(" ")
				.rooms(null)
				.build();

		mockMvc.perform(post("/courses/save").flashAttr("course", course))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("course/edit"));
	}

	@Test
	public void givenCourse_whenDelete_thenCourseIsDeleting() throws Exception {
		Course course = buildCourse();

		mockMvc.perform(post("/courses/{id}/delete", 1))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/courses"));

		verify(courseService).deleteById(course.getId());
	}

	private Course buildCourse() {
		return Course.builder()
				.id(1L)
				.name("Art")
				.rooms(Set.of(Room.builder().id(1L).name("111").capacity(30).build(),
						Room.builder().id(2L).name("222").capacity(30).build()))
				.build();
	}

	private List<Room> buildRooms() {
		return List.of(Room.builder().id(1L).name("111").capacity(30).build(),
				Room.builder().id(2L).name("222").capacity(30).build(),
				Room.builder().id(3L).name("333").capacity(30).build());
	}
}
