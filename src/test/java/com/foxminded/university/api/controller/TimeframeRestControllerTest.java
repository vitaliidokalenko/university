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

import java.time.LocalTime;
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
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.TimeframeService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@WebMvcTest(TimeframeRestController.class)
public class TimeframeRestControllerTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TimeframeService timeframeService;

	@Test
	public void whenGetAll_thenGetRightTimeframesPage() throws Exception {
		Page<Timeframe> expected = new PageImpl<>(List.of(buildTimeframe()));
		when(timeframeService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/api/v1/timeframes").param("page", "0")
				.param("size", "1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expected)))
				.andExpect(status().isOk());
	}

	@Test
	public void givenId_whenGetById_thenGetRightTimeframe() throws Exception {
		Timeframe expected = buildTimeframe();
		when(timeframeService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/api/v1/timeframes/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expected)))
				.andExpect(status().isOk());
	}

	@Test
	public void givenTimeframeIsNotPresent_whenGetById_thenStatusIsNotFound() throws Exception {
		when(timeframeService.findById(1L)).thenThrow(new NotFoundEntityException());

		mockMvc.perform(get("/api/v1/timeframes/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void givenNewTimeframe_whenCreate_thenTimeframeIsCreated() throws Exception {
		Timeframe timeframe = buildTimeframe();

		mockMvc.perform(post("/api/v1/timeframes")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(timeframe)))
				.andExpect(header().string("Location", containsString("/api/v1/timeframes/1")))
				.andExpect(redirectedUrlPattern("http://*/api/v1/timeframes/1"))
				.andExpect(status().isCreated());

		verify(timeframeService).create(timeframe);
	}

	@Test
	public void givenTimeframe_whenUpdate_thenTimeframeIsUpdated() throws Exception {
		Timeframe timeframe = buildTimeframe();

		mockMvc.perform(put("/api/v1/timeframes/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(timeframe)))
				.andExpect(status().isOk());

		verify(timeframeService).update(timeframe);
	}

	@Test
	public void givenId_whenDelete_thenTimeframeIsDeleted() throws Exception {
		mockMvc.perform(delete("/api/v1/timeframes/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(timeframeService).deleteById(1L);
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
