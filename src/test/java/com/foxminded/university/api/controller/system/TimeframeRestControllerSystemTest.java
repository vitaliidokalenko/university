package com.foxminded.university.api.controller.system;

import static org.hamcrest.CoreMatchers.containsString;

import java.time.LocalTime;

import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Timeframe;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FlywayTestExtension
public class TimeframeRestControllerSystemTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private WebTestClient webClient;

	@Test
	public void whenGetAll_thenGetRightTimeframes() {
		Timeframe expected = buildTimeframe();

		webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/timeframes")
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
				.jsonPath("$.content.[0]sequence")
				.isEqualTo(expected.getSequence());
	}

	@Test
	public void givenId_whenGetById_thenGetRightTimeframe() throws Exception {
		Timeframe expected = buildTimeframe();

		webClient.get()
				.uri("/api/v1/timeframes/{id}", 1)
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectBody()
				.json(mapper.writeValueAsString(expected));
	}

	@Test
	public void givenNewTimeframe_whenCreate_thenTimeframeIsCreated() {
		Timeframe timeframe = Timeframe.builder()
				.sequence(5)
				.startTime(LocalTime.parse("14:40"))
				.endTime(LocalTime.parse("16:00"))
				.build();

		webClient.post()
				.uri("/api/v1/timeframes")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(timeframe)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.value("Location", containsString("/api/v1/timeframes/5"));
	}

	@Test
	public void givenTimeframe_whenUpdate_thenTimeframeIsUpdated() {
		Timeframe timeframe = buildTimeframe();

		webClient.put()
				.uri("/api/v1/timeframes/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(timeframe)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	public void givenId_whenDelete_thenTimeframeIsDeleted() {
		webClient.delete()
				.uri("/api/v1/timeframes/{id}", 4)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	private Timeframe buildTimeframe() {
		return Timeframe.builder()
				.id(1L)
				.sequence(1)
				.startTime(LocalTime.parse("08:00"))
				.endTime(LocalTime.parse("09:20"))
				.build();
	}
}
