package com.foxminded.university.api.controller;

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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.exception.NotFoundEntityException;

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
				.andExpect(content().json(mapper.writeValueAsString(expected)))
				.andExpect(status().isOk());
	}

	@Test
	public void givenId_whenGetById_thenGetRightGroup() throws Exception {
		Group expected = buildGroup();
		when(groupService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/api/v1/groups/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expected)))
				.andExpect(status().isOk());
	}

	@Test
	public void givenGroupIsNotPresent_whenGetById_thenStatusIsNotFound() throws Exception {
		when(groupService.findById(1L)).thenThrow(new NotFoundEntityException());

		mockMvc.perform(get("/api/v1/groups/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void givenNewGroup_whenCreate_thenGroupIsCreated() throws Exception {
		Group group = buildGroup();

		mockMvc.perform(post("/api/v1/groups")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(group)))
				.andExpect(header().string("Location", containsString("/api/v1/groups/1")))
				.andExpect(redirectedUrlPattern("http://*/api/v1/groups/1"))
				.andExpect(status().isCreated());

		verify(groupService).create(group);
	}

	@Test
	public void givenGroup_whenUpdate_thenGroupIsUpdated() throws Exception {
		Group group = buildGroup();

		mockMvc.perform(put("/api/v1/groups/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(group)))
				.andExpect(status().isOk());

		verify(groupService).update(group);
	}

	@Test
	public void givenId_whenDelete_thenGroupIsDeleted() throws Exception {
		mockMvc.perform(delete("/api/v1/groups/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

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
