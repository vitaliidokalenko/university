package com.foxminded.university.api.controller.system;

import static org.hamcrest.CoreMatchers.containsString;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Room;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FlywayTestExtension
public class RoomRestControllerSystemTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private WebTestClient webClient;

	@Test
	@FlywayTest
	public void whenGetAll_thenGetRightRooms() {
		Room expected = buildRoom();

		webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/rooms")
						.queryParam("page", "0")
						.queryParam("size", "1")
						.build())
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.content.[0]id")
				.isEqualTo(expected.getId())
				.jsonPath("$.content.[0]name")
				.isEqualTo(expected.getName());
	}

	@Test
	@FlywayTest
	public void givenId_whenGetById_thenGetRightRoom() throws Exception {
		Room expected = buildRoom();

		webClient.get()
				.uri("/api/v1/rooms/{id}", 1)
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectBody()
				.json(mapper.writeValueAsString(expected));
	}

	@Test
	@FlywayTest
	public void givenNewRoom_whenCreate_thenRoomIsCreated() {
		Room room = Room.builder().name("E555").capacity(30).build();

		webClient.post()
				.uri("/api/v1/rooms")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(room)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.value("Location", containsString("/api/v1/rooms/5"));
	}

	@Test
	@FlywayTest
	public void givenRoom_whenUpdate_thenRoomIsUpdated() {
		Room room = buildRoom();

		webClient.put()
				.uri("/api/v1/rooms/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(room)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	@FlywayTest
	public void givenId_whenDelete_thenRoomIsDeleted() {
		webClient.delete()
				.uri("/api/v1/rooms/{id}", 4)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	private Room buildRoom() {
		return Room.builder().id(1L).name("A111").capacity(30).build();
	}
}
