package com.foxminded.university.api.controller.system;

import static org.hamcrest.CoreMatchers.containsString;

import java.time.LocalDate;
import java.util.List;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FlywayTestExtension
public class GroupRestControllerSystemTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private WebTestClient webClient;

	@Test
	@FlywayTest
	public void whenGetAll_thenGetRightGroups() {
		Group expected = buildGroup();

		webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/groups")
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
	public void givenId_whenGetById_thenGetRightGroup() throws Exception {
		Group expected = buildGroup();

		webClient.get()
				.uri("/api/v1/groups/{id}", 1)
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
	public void givenNewGroup_whenCreate_thenGroupIsCreated() {
		Group group = Group.builder()
				.name("ee-55")
				.build();

		webClient.post()
				.uri("/api/v1/groups")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(group)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.value("Location", containsString("/api/v1/groups/5"));
	}

	@Test
	@FlywayTest
	public void givenGroup_whenUpdate_thenGroupIsUpdated() {
		Group group = buildGroup();

		webClient.put()
				.uri("/api/v1/groups/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(group)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	@FlywayTest
	public void givenId_whenDelete_thenGroupIsDeleted() {
		webClient.delete()
				.uri("/api/v1/groups/{id}", 4)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	private Group buildGroup() {
		return Group.builder()
				.id(1L)
				.name("AA-11")
				.students(List.of(Student.builder()
						.id(1L)
						.name("Anna")
						.surname("Dvorecka")
						.phone(null)
						.email(null)
						.address(null)
						.birthDate(LocalDate.parse("2001-01-01"))
						.gender(Gender.FEMALE)
						.build()))
				.build();
	}
}
