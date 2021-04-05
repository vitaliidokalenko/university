package com.foxminded.university.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.controller.exception.ControllerExceptionHandler;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

	@Mock
	private StudentService studentService;
	@Mock
	private CourseService courseService;
	@Mock
	private GroupService groupService;
	@InjectMocks
	private StudentController studentController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUpp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentController)
				.setControllerAdvice(new ControllerExceptionHandler())
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightStudentsPage() throws Exception {
		Page<Student> expected = new PageImpl<>(Arrays.asList(buildStudent()));
		when(studentService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/students").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/students"))
				.andExpect(model().attribute("studentsPage", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightStudent() throws Exception {
		Optional<Student> expected = Optional.of(buildStudent());
		when(studentService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/students/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("student/student"))
				.andExpect(model().attribute("student", expected.get()));
	}

	@Test
	public void givenStudentIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(studentService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/students/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception", "NotFoundEntityException"))
				.andExpect(model().attribute("message", "Cannot find student by id: 1"))
				.andExpect(forwardedUrl("error"));
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
		Optional<Student> expected = Optional.of(buildStudent());
		when(studentService.findById(1L)).thenReturn(expected);
		when(courseService.getAll()).thenReturn(buildCourses());
		when(groupService.getAll()).thenReturn(buildGroups());

		mockMvc.perform(get("/students/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("courses", courseService.getAll()))
				.andExpect(model().attribute("groups", groupService.getAll()))
				.andExpect(model().attribute("genders", Gender.values()))
				.andExpect(model().attribute("student", expected.get()))
				.andExpect(forwardedUrl("student/edit"));
	}

	@Test
	public void givenNewStudent_whenSave_thenStudentIsCreating() throws Exception {
		Student student = Student.builder()
				.courses(new HashSet<>(Arrays.asList(Course.builder().id(1L).build())))
				.group(Group.builder().id(1L).build())
				.build();
		when(groupService.findById(1L)).thenReturn(Optional.of(Group.builder().id(1L).name("AA-11").build()));
		when(courseService.findById(1L)).thenReturn(Optional.of(Course.builder().id(1L).name("Law").build()));

		mockMvc.perform(post("/students/save").flashAttr("student", student))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/students"));

		verify(studentService).create(student);
	}

	@Test
	public void givenStudent_whenSave_thenStudentIsUpdating() throws Exception {
		Student student = Student.builder()
				.id(1L)
				.courses(new HashSet<>(Arrays.asList(Course.builder().id(1L).build())))
				.group(Group.builder().id(1L).build())
				.build();
		when(groupService.findById(1L)).thenReturn(Optional.of(Group.builder().id(1L).name("AA-11").build()));
		when(courseService.findById(1L)).thenReturn(Optional.of(Course.builder().id(1L).name("Law").build()));

		mockMvc.perform(post("/students/save").flashAttr("student", student))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/students"));

		verify(studentService).update(student);
	}

	@Test
	public void givenStudent_whenDelete_thenStudentIsDeleting() throws Exception {
		Student student = buildStudent();

		mockMvc.perform(get("/students/{id}/delete", 1))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/students"));

		verify(studentService).deleteById(student.getId());
	}

	private Student buildStudent() {
		return Student.builder()
				.id(1L)
				.courses(new HashSet<>(Arrays.asList(Course.builder().id(1L).name("Art").build(),
						Course.builder().id(2L).name("Law").build())))
				.gender(Gender.MALE)
				.group(Group.builder().id(1L).name("AA-11").build())
				.build();
	}

	private List<Course> buildCourses() {
		return Arrays.asList(Course.builder().id(1L).name("Law").build(),
				Course.builder().id(2L).name("Biology").build(),
				Course.builder().id(3L).name("Music").build());
	}

	private List<Group> buildGroups() {
		return Arrays.asList(Group.builder().id(1L).name("AA-11").build(),
				Group.builder().id(2L).name("BB-22").build(),
				Group.builder().id(3L).name("CC-33").build());
	}
}
