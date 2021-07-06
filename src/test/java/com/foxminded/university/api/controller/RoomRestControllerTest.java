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
import com.foxminded.university.model.Room;
import com.foxminded.university.service.RoomService;

@RunWith(SpringRunner.class)
@WebMvcTest(RoomRestController.class)
public class RoomRestControllerTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RoomService roomService;

	@Test
	public void whenGetAll_thenGetRightRoomsPage() throws Exception {
		Page<Room> expected = new PageImpl<>(List.of(buildRoom()));
		when(roomService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/api/v1/rooms").param("page", "0")
				.param("size", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content[0].id", is(1)))
				.andExpect(jsonPath("$.content[0].name", is("111")))
				.andExpect(status().isOk());
	}

	@Test
	public void givenId_whenGetById_thenGetRightRoom() throws Exception {
		Room expected = buildRoom();
		when(roomService.findById(1L)).thenReturn(Optional.of(expected));

		mockMvc.perform(get("/api/v1/rooms/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("111")))
				.andExpect(status().isOk());
	}

	@Test
	public void givenRoomIsNotPresent_whenGetById_thenStatusIsBadRequest() throws Exception {
		when(roomService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/v1/rooms/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void givenNewRoom_whenCreate_thenRoomIsCreated() throws Exception {
		Room room = buildRoom();
		room.setId(null);

		mockMvc.perform(post("/api/v1/rooms")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(room)))
				.andExpect(status().isCreated());

		verify(roomService).create(room);
	}

	@Test
	public void givenRoom_whenUpdate_thenRoomIsUpdated() throws Exception {
		Room room = buildRoom();

		mockMvc.perform(put("/api/v1/rooms/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsBytes(room)))
				.andExpect(status().isOk());

		verify(roomService).update(room);
	}

	@Test
	public void givenId_whenDelete_thenRoomIsDeleted() throws Exception {
		mockMvc.perform(delete("/api/v1/rooms/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(roomService).deleteById(1L);
	}

	private Room buildRoom() {
		return Room.builder().id(1L).name("111").capacity(30).build();
	}
}
