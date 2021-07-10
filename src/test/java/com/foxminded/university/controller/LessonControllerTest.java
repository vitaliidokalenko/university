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
import java.util.Optional;
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
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.RoomService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.TimeframeService;

@ExtendWith(MockitoExtension.class)
public class LessonControllerTest {

	@Mock
	private LessonService lessonService;
	@Mock
	private GroupService groupService;
	@Mock
	private TeacherService teacherService;
	@Mock
	private CourseService courseService;
	@Mock
	private RoomService roomService;
	@Mock
	private TimeframeService timeframeService;
	@InjectMocks
	private LessonController lessonController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(lessonController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightLessonsPage() throws Exception {
		Page<Lesson> expected = new PageImpl<>(List.of(buildLesson()));
		when(lessonService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/lessons").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("lesson/lessons"))
				.andExpect(model().attribute("lessonsPage", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightLesson() throws Exception {
		Lesson expected = buildLesson();
		when(lessonService.findById(1L)).thenReturn(Optional.of(expected));

		mockMvc.perform(get("/lessons/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("lesson/lesson"))
				.andExpect(model().attribute("lesson", expected));
	}

	@Test
	public void whenCreate_thenAddedNewLessonAttribute() throws Exception {
		when(groupService.getAll()).thenReturn(buildGroups());
		when(teacherService.getAll()).thenReturn(buildTeachers());
		when(courseService.getAll()).thenReturn(buildCourses());
		when(roomService.getAll()).thenReturn(buildRooms());
		when(timeframeService.getAll()).thenReturn(buildTimeframes());

		mockMvc.perform(get("/lessons/new"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("groups", groupService.getAll()))
				.andExpect(model().attribute("teachers", teacherService.getAll()))
				.andExpect(model().attribute("courses", courseService.getAll()))
				.andExpect(model().attribute("rooms", roomService.getAll()))
				.andExpect(model().attribute("timeframes", timeframeService.getAll()))
				.andExpect(model().attribute("lesson", new Lesson()))
				.andExpect(forwardedUrl("lesson/create"));
	}

	@Test
	public void whenUpdate_thenAddedRightLessonAttribute() throws Exception {
		Lesson expected = buildLesson();
		when(lessonService.findById(1L)).thenReturn(Optional.of(expected));
		when(groupService.getAll()).thenReturn(buildGroups());
		when(teacherService.getAll()).thenReturn(buildTeachers());
		when(courseService.getAll()).thenReturn(buildCourses());
		when(roomService.getAll()).thenReturn(buildRooms());
		when(timeframeService.getAll()).thenReturn(buildTimeframes());

		mockMvc.perform(get("/lessons/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("groups", groupService.getAll()))
				.andExpect(model().attribute("teachers", teacherService.getAll()))
				.andExpect(model().attribute("courses", courseService.getAll()))
				.andExpect(model().attribute("rooms", roomService.getAll()))
				.andExpect(model().attribute("timeframes", timeframeService.getAll()))
				.andExpect(model().attribute("lesson", expected))
				.andExpect(forwardedUrl("lesson/edit"));
	}

	@Test
	public void givenNewLesson_whenSave_thenLessonIsCreating() throws Exception {
		Lesson lesson = buildLesson();
		lesson.setId(null);
		when(courseService.findById(1L)).thenReturn(Optional.of(buildCourses().get(0)));
		when(roomService.findById(1L)).thenReturn(Optional.of(buildRooms().get(0)));
		when(teacherService.findById(1L)).thenReturn(Optional.of(buildTeachers().get(0)));
		when(timeframeService.findById(1L)).thenReturn(Optional.of(buildTimeframes().get(0)));
		when(groupService.findById(1L)).thenReturn(Optional.of(buildGroups().get(0)));

		mockMvc.perform(post("/lessons/save").flashAttr("lesson", lesson))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/lessons"));

		verify(lessonService).create(lesson);
	}

	@Test
	public void givenLesson_whenSave_thenLessonIsUpdating() throws Exception {
		Lesson lesson = buildLesson();
		when(courseService.findById(1L)).thenReturn(Optional.of(buildCourses().get(0)));
		when(roomService.findById(1L)).thenReturn(Optional.of(buildRooms().get(0)));
		when(teacherService.findById(1L)).thenReturn(Optional.of(buildTeachers().get(0)));
		when(timeframeService.findById(1L)).thenReturn(Optional.of(buildTimeframes().get(0)));
		when(groupService.findById(1L)).thenReturn(Optional.of(buildGroups().get(0)));

		mockMvc.perform(post("/lessons/save").flashAttr("lesson", lesson))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/lessons"));

		verify(lessonService).update(lesson);
	}

	@Test
	public void givenNotValidLesson_whenSave_thenForwardedEditView() throws Exception {
		Lesson lesson = buildLesson();
		lesson.setTeacher(null);

		mockMvc.perform(post("/lessons/save").flashAttr("lesson", lesson))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("lesson/edit"));
	}

	@Test
	public void givenLesson_whenDelete_thenLessonIsDeleting() throws Exception {
		Lesson lesson = buildLesson();

		mockMvc.perform(post("/lessons/{id}/delete", 1))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/lessons"));

		verify(lessonService).deleteById(lesson.getId());
	}

	@Test
	public void givenTeacherAndDatesAndNotSubstituteTeacherId_whenReplaceTeacher_thenTeacherIsReplacing()
			throws Exception {
		Teacher teacher = buildTeachers().get(0);
		when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));

		mockMvc.perform(
				post("/lessons/replace/teacher").param("teacherId", "1")
						.param("startDate", "2021-01-21")
						.param("endDate", "2021-01-21"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/lessons"));

		verify(lessonService).replaceTeacherByDateBetween(teacher,
				LocalDate.parse("2021-01-21"),
				LocalDate.parse("2021-01-21"),
				null);
	}

	@Test
	public void givenTeacherAndDatesAndSubstituteTeacherId_whenReplaceTeacher_thenTeacherIsReplacing()
			throws Exception {
		Teacher teacher = buildTeachers().get(0);
		when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));

		mockMvc.perform(
				post("/lessons/replace/teacher").param("teacherId", "1")
						.param("startDate", "2021-01-21")
						.param("endDate", "2021-01-21")
						.param("substituteTeacherId", "2"))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/lessons"));

		verify(lessonService).replaceTeacherByDateBetween(teacher,
				LocalDate.parse("2021-01-21"),
				LocalDate.parse("2021-01-21"),
				List.of(2L));
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
				.date(LocalDate.parse("2070-01-21"))
				.groups(groups)
				.room(room)
				.teacher(teacher)
				.timeframe(timeframe)
				.build();
	}

	private List<Group> buildGroups() {
		return List.of(Group.builder().id(1L).name("AA-11").build(),
				Group.builder().id(2L).name("BB-22").build(),
				Group.builder().id(3L).name("CC-33").build());
	}

	private List<Teacher> buildTeachers() {
		return List.of(Teacher.builder().id(1L).name("Homer").surname("Simpson").build(),
				Teacher.builder().id(2L).name("Aleksandra").surname("Ivanova").build(),
				Teacher.builder().id(3L).name("Anatoly").surname("Sviridov").build());
	}

	private List<Course> buildCourses() {
		return List.of(Course.builder().id(1L).name("Art").build(),
				Course.builder().id(2L).name("Law").build(),
				Course.builder().id(3L).name("Music").build());
	}

	private List<Room> buildRooms() {
		return List.of(Room.builder().id(1L).name("111").capacity(30).build(),
				Room.builder().id(2L).name("222").capacity(30).build(),
				Room.builder().id(3L).name("333").capacity(30).build());
	}

	private List<Timeframe> buildTimeframes() {
		return List.of(
				Timeframe.builder().id(1L).sequence(1).startTime(parse("08:00")).endTime(parse("09:20")).build(),
				Timeframe.builder().id(2L).sequence(2).startTime(parse("09:40")).endTime(parse("11:00")).build(),
				Timeframe.builder().id(3L).sequence(3).startTime(parse("11:20")).endTime(parse("12:40")).build());
	}
}
