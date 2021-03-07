package com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
				.setControllerAdvice(new ExceptionHandlingController())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightListOfRooms() throws Exception {
		List<Room> expected = Arrays.asList(buildRoom());
		when(roomService.getAll()).thenReturn(expected);

		mockMvc.perform(get("/rooms"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("room/rooms"))
				.andExpect(model().attribute("rooms", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightRoom() throws Exception {
		Optional<Room> expected = Optional.of(buildRoom());
		when(roomService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/rooms/1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("room/room"))
				.andExpect(model().attribute("room", expected.get()));
	}

	@Test
	public void givenRoomIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(roomService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/rooms/1"))
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
