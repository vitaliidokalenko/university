package com.foxminded.university.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
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

import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.TimeframeService;

@ExtendWith(MockitoExtension.class)
public class TimeframeControllerTest {

	@Mock
	private TimeframeService timeframeService;
	@InjectMocks
	private TimeframeController timeframeController;
	private MockMvc mockMvc;

	@BeforeEach
	void setUpp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(timeframeController)
				.setControllerAdvice(new ExceptionHandlingController())
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void whenGetAll_thenGetRightTimeframesPages() throws Exception {
		Page<Timeframe> expected = new PageImpl<>(Arrays.asList(buildTimeframe()));
		when(timeframeService.getAllPage(PageRequest.of(0, 1))).thenReturn(expected);

		mockMvc.perform(get("/timeframes").param("page", "0").param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("timeframe/timeframes"))
				.andExpect(model().attribute("timeframesPage", expected))
				.andExpect(model().attribute("numbers", IntStream.rangeClosed(1, expected.getTotalPages()).toArray()));
	}

	@Test
	public void givenId_whenFindById_thenGetRightTimeframe() throws Exception {
		Optional<Timeframe> expected = Optional.of(buildTimeframe());
		when(timeframeService.findById(1L)).thenReturn(expected);

		mockMvc.perform(get("/timeframes/1"))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("timeframe/timeframe"))
				.andExpect(model().attribute("timeframe", expected.get()));
	}

	@Test
	public void givenTimeframeIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(timeframeService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/timeframes/1"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception", "NotFoundEntityException"))
				.andExpect(model().attribute("message", "Cannot find timeframe by id: 1"))
				.andExpect(forwardedUrl("error"));
	}

	private Timeframe buildTimeframe() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequence(1);
		timeframe.setStartTime(LocalTime.parse("08:00"));
		timeframe.setEndTime(LocalTime.parse("09:20"));
		return timeframe;
	}
}
