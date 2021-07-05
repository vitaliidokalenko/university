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
import com.foxminded.university.api.controller.GroupRestController;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;

@RunWith(SpringRunner.class)
@WebMvcTest(GroupRestController.class)
public class GroupRestControllerTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private GroupService groupService;

	@Test
	public void whenGetAll_thenGetRightGroupsPage() throws Exception {
		Page<Group> expected = new PageImpl<>(List.of(buildGroup()));
		when(groupService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/api/v1/groups").param("page", "0")
				.param("size", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content[0].id", is(1)))
				.andExpect(jsonPath("$.content[0].name", is("AA-11")))
				.andExpect(status().isOk());
	}

	@Test
	public void givenId_whenGetById_thenGetRightGroup() throws Exception {
		Group expected = buildGroup();
		when(groupService.findById(1L)).thenReturn(Optional.of(expected));

		mockMvc.perform(get("/api/v1/groups/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("AA-11")))
				.andExpect(status().isOk());
	}

	@Test
	public void givenGroupIsNotPresent_whenGetById_thenStatusIsBadRequest() throws Exception {
		when(groupService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/v1/groups/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenNewGroup_whenCreate_thenGroupIsCreated() throws Exception {
		Group group = buildGroup();
		group.setId(null);

		mockMvc.perform(post("/api/v1/groups")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(group)))
				.andExpect(status().isCreated());

		verify(groupService).create(group);
	}

	@Test
	public void givenGroup_whenUpdate_thenGroupIsUpdated() throws Exception {
		Group group = buildGroup();

		mockMvc.perform(put("/api/v1/groups/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(group)))
				.andExpect(status().isOk());

		verify(groupService).update(group);
	}

	@Test
	public void givenId_whenDelete_thenGroupIsDeleted() throws Exception {
		mockMvc.perform(delete("/api/v1/groups/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		verify(groupService).deleteById(1L);
	}

	private Group buildGroup() {
		return Group.builder()
				.id(1L)
				.name("AA-11")
				.students(List.of(Student.builder().id(1L).name("Anatoly").surname("Chegrinets").build()))
				.build();
	}
}
