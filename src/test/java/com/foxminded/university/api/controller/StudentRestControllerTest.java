package com.foxminded.university.api.controller;

import static java.time.LocalTime.parse;
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

import java.time.LocalDate;
import java.util.List;
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
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.StudentService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentRestController.class)
public class StudentRestControllerTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private StudentService studentService;
	@MockBean
	private LessonService lessonService;

	@Test
	public void whenGetAll_thenGetRightStudentsPage() throws Exception {
		Page<Student> expected = new PageImpl<>(List.of(buildStudent()));
		when(studentService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/api/v1/students").param("page", "0")
				.param("size", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expected)))
				.andExpect(status().isOk());
	}

	@Test
	public void givenId_whenGetById_thenGetRightStudent() throws Exception {
		Student expected = buildStudent();
		when(studentService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/api/v1/students/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expected)))
				.andExpect(status().isOk());
	}

	@Test
	public void givenStudentIsNotPresent_whenGetById_thenStatusIsNotFound() throws Exception {
		when(studentService.findById(1L)).thenThrow(new NotFoundEntityException());
		mockMvc.perform(get("/api/v1/students/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void givenNewStudent_whenCreate_thenStudentIsCreated() throws Exception {
		Student student = buildStudent();

		mockMvc.perform(post("/api/v1/students")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(student)))
				.andExpect(header().string("Location", containsString("/api/v1/students/1")))
				.andExpect(redirectedUrlPattern("http://*/api/v1/students/1"))
				.andExpect(status().isCreated());

		verify(studentService).create(student);
	}

	@Test
	public void givenStudent_whenUpdate_thenStudentIsUpdated() throws Exception {
		Student student = buildStudent();

		mockMvc.perform(put("/api/v1/students/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(student)))
				.andExpect(status().isOk());

		verify(studentService).update(student);
	}

	@Test
	public void givenId_whenDelete_thenStudentIsDeleted() throws Exception {
		mockMvc.perform(delete("/api/v1/students/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(studentService).deleteById(1L);
	}

	@Test
	public void givenDates_whenGetTimetable_thenGetRightLessons() throws Exception {
		Student student = buildStudent();
		List<Lesson> expected = List.of(buildLesson());
		LocalDate startDate = LocalDate.parse("2021-01-21");
		LocalDate endDate = LocalDate.parse("2021-01-21");
		when(studentService.findById(1L)).thenReturn(student);
		when(lessonService.getByGroupAndDateBetween(student.getGroup(), startDate, endDate)).thenReturn(expected);

		mockMvc.perform(
				get("/api/v1/students/{id}/timetable", 1).param("startDate", "2021-01-21")
						.param("endDate", "2021-01-21")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expected)))
				.andExpect(status().isOk());
	}

	private Student buildStudent() {
		return Student.builder()
				.id(1L)
				.name("Homer")
				.surname("Simpson")
				.birthDate(LocalDate.parse("1995-01-01"))
				.courses(Set.of(Course.builder().id(1L).name("Art").build(),
						Course.builder().id(2L).name("Law").build()))
				.gender(Gender.MALE)
				.group(Group.builder().id(1L).name("AA-11").build())
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
