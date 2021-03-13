package com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

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
				.andExpect(model().attribute("roomsPage", expected))
				.andExpect(model().attribute("numbers", IntStream.rangeClosed(1, expected.getTotalPages()).toArray()));
	}

	@Test
	public void givenId_whenFindById_thenGetRightRoom() throws Exception {
		Optional<Room> expected = Optional.of(buildRoom());
		when(roomService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/rooms/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("room/room"))
				.andExpect(model().attribute("room", expected.get()));
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

	private Room buildRoom() {
		Room room = new Room("111");
		room.setId(1L);
		room.setCapacity(30);
		return room;
	}
}
