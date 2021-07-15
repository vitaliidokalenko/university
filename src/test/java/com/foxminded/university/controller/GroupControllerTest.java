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

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTest {

	@Mock
	private GroupService groupService;
	@InjectMocks
	private GroupController groupController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(groupController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightGroupsPage() throws Exception {
		Page<Group> expected = new PageImpl<>(List.of(buildGroup()));
		when(groupService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/groups").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("group/groups"))
				.andExpect(model().attribute("groupsPage", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightGroup() throws Exception {
		Group expected = buildGroup();
		when(groupService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/groups/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("group/group"))
				.andExpect(model().attribute("group", expected));
	}

	@Test
	public void whenCreate_thenAddedNewStudentAttribute() throws Exception {
		mockMvc.perform(get("/groups/new"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("group", new Group()))
				.andExpect(forwardedUrl("group/create"));
	}

	@Test
	public void whenUpdate_thenAddedRightGroupAttribute() throws Exception {
		Group expected = buildGroup();
		when(groupService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/groups/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("group", expected))
				.andExpect(forwardedUrl("group/edit"));
	}

	@Test
	public void givenNewGroup_whenSave_thenGroupIsCreating() throws Exception {
		Group group = Group.builder()
				.name("AA-11")
				.build();

		mockMvc.perform(post("/groups/save").flashAttr("group", group))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/groups"));

		verify(groupService).create(group);
	}

	@Test
	public void givenGroup_whenSave_thenGroupIsUpdating() throws Exception {
		Group group = Group.builder()
				.id(1L)
				.name("AA-11")
				.build();

		mockMvc.perform(post("/groups/save").flashAttr("group", group))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/groups"));

		verify(groupService).update(group);
	}

	@Test
	public void givenNotValidGroup_whenSave_thenForwardedEditView() throws Exception {
		Group group = Group.builder()
				.name(" ")
				.build();

		mockMvc.perform(post("/groups/save").flashAttr("group", group))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("group/edit"));
	}

	@Test
	public void givenGroup_whenDelete_thenGroupIsDeleting() throws Exception {
		Group group = buildGroup();

		mockMvc.perform(post("/groups/{id}/delete", 1))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/groups"));

		verify(groupService).deleteById(group.getId());
	}

	private Group buildGroup() {
		return Group.builder()
				.id(1L)
				.name("AA-11")
				.students(List.of(Student.builder().id(1L).name("Anatoly").surname("Chegrinets").build()))
				.build();
	}
}
