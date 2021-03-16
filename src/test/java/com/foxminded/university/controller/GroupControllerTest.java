package com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
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
	void setUpp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(groupController)
				.setControllerAdvice(new ControllerExceptionHandler())
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightGroupsPage() throws Exception {
		Page<Group> expected = new PageImpl<>(Arrays.asList(buildGroup()));
		when(groupService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/groups").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("group/groups"))
				.andExpect(model().attribute("groupsPage", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightGroup() throws Exception {
		Optional<Group> expected = Optional.of(buildGroup());
		when(groupService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/groups/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("group/group"))
				.andExpect(model().attribute("group", expected.get()));
	}

	@Test
	public void givenGroupIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(groupService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/groups/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception", "NotFoundEntityException"))
				.andExpect(model().attribute("message", "Cannot find group by id: 1"))
				.andExpect(forwardedUrl("error"));
	}

	private Group buildGroup() {
		Group group = new Group("AA-11");
		group.setId(1L);
		group.setStudents(Arrays.asList(new Student("Anatoy", "Chegrinets")));
		return group;
	}
}
