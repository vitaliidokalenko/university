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
import com.foxminded.university.model.Room;
import com.foxminded.university.service.RoomService;

@ExtendWith(MockitoExtension.class)
public class RoomControllerTest {

	@Mock
	private RoomService roomService;
	@InjectMocks
	private RoomController roomController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUpp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(roomController)
				.setControllerAdvice(new ControllerExceptionHandler())
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightRoomsPage() throws Exception {
		Page<Room> expected = new PageImpl<>(Arrays.asList(buildRoom()));
		when(roomService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/rooms").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("room/rooms"))
				.andExpect(model().attribute("roomsPage", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightRoom() throws Exception {
		Room expected = buildRoom();
		when(roomService.findById(1L)).thenReturn(Optional.of(expected));

		mockMvc.perform(get("/rooms/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("room/room"))
				.andExpect(model().attribute("room", expected));
	}

	@Test
	public void givenRoomIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(roomService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/rooms/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception", "NotFoundEntityException"))
				.andExpect(model().attribute("message", "Cannot find room by id: 1"))
				.andExpect(forwardedUrl("error"));
	}

	@Test
	public void whenCreate_thenAddedNewRoomAttribute() throws Exception {
		mockMvc.perform(get("/rooms/new"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("room", new Room()))
				.andExpect(forwardedUrl("room/create"));
	}

	@Test
	public void whenUpdate_thenAddedRightRoomAttribute() throws Exception {
		Room expected = buildRoom();
		when(roomService.findById(1L)).thenReturn(Optional.of(expected));

		mockMvc.perform(get("/rooms/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("room", expected))
				.andExpect(forwardedUrl("room/edit"));
	}

	@Test
	public void givenNewRoom_whenSave_thenRoomIsCreating() throws Exception {
		Room room = Room.builder().name("111").capacity(30).build();

		mockMvc.perform(post("/rooms/save").flashAttr("room", room))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/rooms"));

		verify(roomService).create(room);
	}

	@Test
	public void givenRoom_whenSave_thenRoomIsUpdating() throws Exception {
		Room room = Room.builder().id(1L).name("111").capacity(30).build();

		mockMvc.perform(post("/rooms/save").flashAttr("room", room))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/rooms"));

		verify(roomService).update(room);
	}

	@Test
	public void givenRoom_whenDelete_thenRoomIsDeleting() throws Exception {
		Room room = buildRoom();

		mockMvc.perform(post("/rooms/{id}/delete", 1))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/rooms"));

		verify(roomService).deleteById(room.getId());
	}

	private Room buildRoom() {
		return Room.builder().id(1L).name("111").capacity(30).build();
	}
}
