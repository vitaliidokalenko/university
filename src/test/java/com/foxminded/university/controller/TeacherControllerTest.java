package com.foxminded.university.controller;

import static java.time.LocalTime.parse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.TeacherService;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

	@Mock
	private TeacherService teacherService;
	@Mock
	private CourseService courseService;
	@Mock
	private LessonService lessonService;
	@InjectMocks
	private TeacherController teacherController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(teacherController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightTeachersPage() throws Exception {
		Page<Teacher> expected = new PageImpl<>(List.of(buildTeacher()));
		when(teacherService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/teachers").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("teacher/teachers"))
				.andExpect(model().attribute("teachersPage", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightTeacher() throws Exception {
		Teacher expected = buildTeacher();
		when(teacherService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/teachers/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("teacher/teacher"))
				.andExpect(model().attribute("teacher", expected));
	}

	@Test
	public void whenCreate_thenAddedNewTeacherAttribute() throws Exception {
		when(courseService.getAll()).thenReturn(buildCourses());

		mockMvc.perform(get("/teachers/new"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("courses", courseService.getAll()))
				.andExpect(model().attribute("genders", Gender.values()))
				.andExpect(model().attribute("teacher", new Teacher()))
				.andExpect(forwardedUrl("teacher/create"));
	}

	@Test
	public void whenUpdate_thenAddedRightTeacherAttribute() throws Exception {
		Teacher expected = buildTeacher();
		when(teacherService.findById(1L)).thenReturn(expected);
		when(courseService.getAll()).thenReturn(buildCourses());

		mockMvc.perform(get("/teachers/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("courses", courseService.getAll()))
				.andExpect(model().attribute("genders", Gender.values()))
				.andExpect(model().attribute("teacher", expected))
				.andExpect(forwardedUrl("teacher/edit"));
	}

	@Test
	public void givenNewTeacher_whenSave_thenTeacherIsCreating() throws Exception {
		Teacher teacher = buildTeacher();
		teacher.setId(null);
		when(courseService.findById(1L)).thenReturn(Course.builder().id(1L).name("Art").build());
		when(courseService.findById(2L)).thenReturn(Course.builder().id(2L).name("Law").build());

		mockMvc.perform(post("/teachers/save").flashAttr("teacher", teacher))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/teachers"));

		verify(teacherService).create(teacher);
	}

	@Test
	public void givenTeacher_whenSave_thenTeacherIsUpdating() throws Exception {
		Teacher teacher = buildTeacher();
		when(courseService.findById(1L)).thenReturn(Course.builder().id(1L).name("Art").build());
		when(courseService.findById(2L)).thenReturn(Course.builder().id(2L).name("Law").build());

		mockMvc.perform(post("/teachers/save").flashAttr("teacher", teacher))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/teachers"));

		verify(teacherService).update(teacher);
	}

	@Test
	public void givenNotValidTeacher_whenSave_thenForwardedEditView() throws Exception {
		Teacher teacher = buildTeacher();
		teacher.setName(null);

		mockMvc.perform(post("/teachers/save").flashAttr("teacher", teacher))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("teacher/edit"));
	}

	@Test
	public void givenTeacher_whenDelete_thenTeacherIsDeleting() throws Exception {
		Teacher teacher = buildTeacher();

		mockMvc.perform(post("/teachers/{id}/delete", 1))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/teachers"));

		verify(teacherService).deleteById(teacher.getId());
	}

	@Test
	public void givenDates_whenGetTimetable_thenGetRightLessons() throws Exception {
		Teacher teacher = buildTeacher();
		List<Lesson> expected = List.of(buildLesson());
		LocalDate startDate = LocalDate.parse("2021-01-21");
		LocalDate endDate = LocalDate.parse("2021-01-21");
		when(teacherService.findById(1L)).thenReturn(teacher);
		when(lessonService.getByTeacherAndDateBetween(teacher, startDate, endDate))
				.thenReturn(expected);

		mockMvc.perform(
				get("/teachers/{id}/timetable", 1).param("startDate", "2021-01-21").param("endDate", "2021-01-21"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("teacher/timetable"))
				.andExpect(model().attribute("teacher", teacher))
				.andExpect(model().attribute("lessons", expected))
				.andExpect(model().attribute("startDate", startDate))
				.andExpect(model().attribute("endDate", endDate));
	}

	@Test
	public void whenReplace_thenRequestForwardedReplaceView() throws Exception {
		Teacher teacher = buildTeacher();
		Teacher substituteTeacher = buildTeacher();
		substituteTeacher.setId(2L);
		Set<Teacher> substituteTeachers = Set.of(substituteTeacher);
		when(teacherService.findById(1L)).thenReturn(teacher);
		when(teacherService.getSubstituteTeachers(teacher)).thenReturn(substituteTeachers);

		mockMvc.perform(get("/teachers/{id}/replace", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("teacher", teacher))
				.andExpect(model().attribute("substituteTeachers", substituteTeachers))
				.andExpect(forwardedUrl("teacher/replace"));
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

	private List<Course> buildCourses() {
		return List.of(Course.builder().id(1L).name("Art").build(),
				Course.builder().id(2L).name("Law").build(),
				Course.builder().id(3L).name("Music").build());
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
