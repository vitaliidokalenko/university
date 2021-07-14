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
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

	@Mock
	private StudentService studentService;
	@Mock
	private CourseService courseService;
	@Mock
	private GroupService groupService;
	@Mock
	private LessonService lessonService;
	@InjectMocks
	private StudentController studentController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightStudentsPage() throws Exception {
		Page<Student> expected = new PageImpl<>(List.of(buildStudent()));
		when(studentService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/students").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/students"))
				.andExpect(model().attribute("studentsPage", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightStudent() throws Exception {
		Student expected = buildStudent();
		when(studentService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/students/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/student"))
				.andExpect(model().attribute("student", expected));
	}

	@Test
	public void whenCreate_thenAddedNewStudentAttribute() throws Exception {
		when(courseService.getAll()).thenReturn(buildCourses());
		when(groupService.getAll()).thenReturn(buildGroups());

		mockMvc.perform(get("/students/new"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("courses", courseService.getAll()))
				.andExpect(model().attribute("groups", groupService.getAll()))
				.andExpect(model().attribute("genders", Gender.values()))
				.andExpect(model().attribute("student", new Student()))
				.andExpect(forwardedUrl("student/create"));
	}

	@Test
	public void whenUpdate_thenAddedRightStudentAttribute() throws Exception {
		Student expected = buildStudent();
		when(studentService.findById(1L)).thenReturn(expected);
		when(courseService.getAll()).thenReturn(buildCourses());
		when(groupService.getAll()).thenReturn(buildGroups());

		mockMvc.perform(get("/students/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("courses", courseService.getAll()))
				.andExpect(model().attribute("groups", groupService.getAll()))
				.andExpect(model().attribute("genders", Gender.values()))
				.andExpect(model().attribute("student", expected))
				.andExpect(forwardedUrl("student/edit"));
	}

	@Test
	public void givenNewStudent_whenSave_thenStudentIsCreating() throws Exception {
		Student student = buildStudent();
		student.setId(null);
		when(groupService.findById(1L)).thenReturn(Group.builder().id(1L).name("AA-11").build());
		when(courseService.findById(1L)).thenReturn(Course.builder().id(1L).name("Art").build());
		when(courseService.findById(2L)).thenReturn(Course.builder().id(2L).name("Law").build());

		mockMvc.perform(post("/students/save").flashAttr("student", student))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/students"));

		verify(studentService).create(student);
	}

	@Test
	public void givenStudent_whenSave_thenStudentIsUpdating() throws Exception {
		Student student = buildStudent();
		when(groupService.findById(1L)).thenReturn(Group.builder().id(1L).name("AA-11").build());
		when(courseService.findById(1L)).thenReturn(Course.builder().id(1L).name("Art").build());
		when(courseService.findById(2L)).thenReturn(Course.builder().id(2L).name("Law").build());

		mockMvc.perform(post("/students/save").flashAttr("student", student))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/students"));

		verify(studentService).update(student);
	}

	@Test
	public void givenNotValidStudent_whenSave_thenForwardedEditView() throws Exception {
		Student student = buildStudent();
		student.setName(null);

		mockMvc.perform(post("/students/save").flashAttr("student", student))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/edit"));
	}

	@Test
	public void givenStudent_whenDelete_thenStudentIsDeleting() throws Exception {
		Student student = buildStudent();

		mockMvc.perform(post("/students/{id}/delete", 1))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/students"));

		verify(studentService).deleteById(student.getId());
	}

	@Test
	public void givenDates_whenGetTimetable_thenGetRightLessons() throws Exception {
		Student student = buildStudent();
		List<Lesson> expected = List.of(buildLesson());
		LocalDate startDate = LocalDate.parse("2021-01-21");
		LocalDate endDate = LocalDate.parse("2021-01-21");
		when(studentService.findById(1L)).thenReturn(student);
		when(lessonService.getByGroupAndDateBetween(student.getGroup(), startDate, endDate))
				.thenReturn(expected);

		mockMvc.perform(
				get("/students/{id}/timetable", 1).param("startDate", "2021-01-21").param("endDate", "2021-01-21"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/timetable"))
				.andExpect(model().attribute("student", student))
				.andExpect(model().attribute("lessons", expected))
				.andExpect(model().attribute("startDate", startDate))
				.andExpect(model().attribute("endDate", endDate));
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

	private List<Course> buildCourses() {
		return List.of(Course.builder().id(1L).name("Law").build(),
				Course.builder().id(2L).name("Biology").build(),
				Course.builder().id(3L).name("Music").build());
	}

	private List<Group> buildGroups() {
		return List.of(Group.builder().id(1L).name("AA-11").build(),
				Group.builder().id(2L).name("BB-22").build(),
				Group.builder().id(3L).name("CC-33").build());
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
