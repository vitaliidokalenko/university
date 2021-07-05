package com.foxminded.university.api.controller;

import static java.time.LocalTime.parse;
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

import java.time.LocalDate;
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
import com.foxminded.university.api.controller.TeacherRestController;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.TeacherService;

@RunWith(SpringRunner.class)
@WebMvcTest(TeacherRestController.class)
public class TeacherRestControllerTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TeacherService teacherService;
	@MockBean
	private LessonService lessonService;

	@Test
	public void whenGetAll_thenGetRightTeachersPage() throws Exception {
		Page<Teacher> expected = new PageImpl<>(List.of(buildTeacher()));
		when(teacherService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/api/v1/teachers").param("page", "0")
				.param("size", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content[0].id", is(1)))
				.andExpect(jsonPath("$.content[0].name", is("Homer")))
				.andExpect(status().isOk());
	}

	@Test
	public void givenId_whenGetById_thenGetRightTeacher() throws Exception {
		Teacher expected = buildTeacher();
		when(teacherService.findById(1L)).thenReturn(Optional.of(expected));

		mockMvc.perform(get("/api/v1/teachers/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("Homer")))
				.andExpect(status().isOk());
	}

	@Test
	public void givenTeacherIsNotPresent_whenGetById_thenStatusIsBadRequest() throws Exception {
		when(teacherService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/v1/teachers/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenNewTeacher_whenCreate_thenTeacherIsCreated() throws Exception {
		Teacher teacher = buildTeacher();
		teacher.setId(null);

		mockMvc.perform(post("/api/v1/teachers")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(teacher)))
				.andExpect(status().isCreated());

		verify(teacherService).create(teacher);
	}

	@Test
	public void givenTeacher_whenUpdate_thenTeacherIsUpdated() throws Exception {
		Teacher teacher = buildTeacher();

		mockMvc.perform(put("/api/v1/teachers/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(teacher)))
				.andExpect(status().isOk());

		verify(teacherService).update(teacher);
	}

	@Test
	public void givenId_whenDelete_thenTeacherIsDeleted() throws Exception {
		mockMvc.perform(delete("/api/v1/teachers/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(teacherService).deleteById(1L);
	}

	@Test
	public void givenDates_whenGetTimetable_thenGetRightLessons() throws Exception {
		Teacher teacher = buildTeacher();
		List<Lesson> expected = List.of(buildLesson());
		LocalDate startDate = LocalDate.parse("2021-01-21");
		LocalDate endDate = LocalDate.parse("2021-01-21");
		when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));
		when(lessonService.getByTeacherAndDateBetween(teacher, startDate, endDate)).thenReturn(expected);

		mockMvc.perform(
				get("/api/v1/teachers/{id}/timetable", 1).param("startDate", "2021-01-21")
						.param("endDate", "2021-01-21")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].date", is("2021-01-21")))
				.andExpect(jsonPath("$[0].teacher.name", is("Homer")))
				.andExpect(jsonPath("$[0].course.name", is("Art")))
				.andExpect(jsonPath("$[0].room.name", is("111")))
				.andExpect(jsonPath("$[0].timeframe.startTime", is("08:00:00")))
				.andExpect(status().isOk());
	}

	@Test
	public void givenId_whenGetSubstitutes_thenGetRightSubstitutesTeachers() throws Exception {
		Teacher teacher = buildTeacher();
		Teacher substituteTeacher = Teacher.builder()
				.id(2L)
				.name("Andrii")
				.surname("Salov")
				.email("andrii_salov@gmail.com")
				.birthDate(LocalDate.parse("1986-07-07"))
				.courses(Set.of(Course.builder().id(1L).name("Art").build(),
						Course.builder().id(2L).name("Law").build()))
				.gender(Gender.MALE)
				.build();
		Set<Teacher> substituteTeachers = Set.of(substituteTeacher);
		when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));
		when(teacherService.getSubstituteTeachers(teacher)).thenReturn(substituteTeachers);

		mockMvc.perform(
				get("/api/v1/teachers/{id}/substitutes", 1)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(2)))
				.andExpect(jsonPath("$[0].name", is("Andrii")))
				.andExpect(jsonPath("$[0].surname", is("Salov")))
				.andExpect(jsonPath("$[0].email", is("andrii_salov@gmail.com")))
				.andExpect(jsonPath("$[0].birthDate", is("1986-07-07")))
				.andExpect(jsonPath("$[0].gender", is("MALE")))
				.andExpect(status().isOk());
	}

	private Teacher buildTeacher() {
		return Teacher.builder()
				.id(1L)
				.name("Homer")
				.surname("Simpson")
				.email("simpson@gmail.com")
				.birthDate(LocalDate.parse("1988-11-11"))
				.courses(Set.of(Course.builder().id(1L).name("Art").build(),
						Course.builder().id(2L).name("Law").build()))
				.gender(Gender.MALE)
				.build();
	}

	private Lesson buildLesson() {
		Room room = Room.builder().id(1L).name("111").capacity(30).build();
		Course course = Course.builder().id(1L).name("Art").rooms(Set.of(room)).build();
		Set<Group> groups = Set.of(Group.builder().id(1L).name("AA-11").build());
		Teacher teacher = Teacher.builder()
				.id(1L)
				.name("Homer")
				.surname("Simpson")
				.courses(Set.of(course))
				.build();
		Timeframe timeframe = Timeframe.builder()
				.id(1L)
				.sequence(1)
				.startTime(parse("08:00"))
				.endTime(parse("09:20"))
				.build();
		return Lesson.builder()
				.id(1L)
				.course(course)
				.date(LocalDate.parse(
						"2021-01-21"))
				.groups(groups)
				.room(room)
				.teacher(teacher)
				.timeframe(timeframe)
				.build();
	}
}
