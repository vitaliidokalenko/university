package com.foxminded.university.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
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
				.setControllerAdvice(new ControllerExceptionHandler())
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
				.andExpect(model().attribute("timeframesPage", expected));
	}

	@Test
	public void givenId_whenFindById_thenGetRightTimeframe() throws Exception {
		Timeframe expected = buildTimeframe();
		when(timeframeService.findById(1L)).thenReturn(Optional.of(expected));

		mockMvc.perform(get("/timeframes/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(forwardedUrl("timeframe/timeframe"))
				.andExpect(model().attribute("timeframe", expected));
	}

	@Test
	public void givenTimeframeIsNotPresent_whenFindById_thenRequestForwardedErrorView() throws Exception {
		when(timeframeService.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/timeframes/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("exception", "NotFoundEntityException"))
				.andExpect(model().attribute("message", "Cannot find timeframe by id: 1"))
				.andExpect(forwardedUrl("error"));
	}

	@Test
	public void whenCreate_thenAddedNewTimeframeAttribute() throws Exception {

		mockMvc.perform(get("/timeframes/new"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("timeframe", new Timeframe()))
				.andExpect(forwardedUrl("timeframe/create"));
	}

	@Test
	public void whenUpdate_thenAddedRightTimeframeAttribute() throws Exception {
		Timeframe expected = buildTimeframe();
		when(timeframeService.findById(1L)).thenReturn(Optional.of(expected));

		mockMvc.perform(get("/timeframes/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("timeframe", expected))
				.andExpect(forwardedUrl("timeframe/edit"));
	}

	@Test
	public void givenNewTimeframe_whenSave_thenTimeframeIsCreating() throws Exception {
		Timeframe timeframe = new Timeframe();

		mockMvc.perform(post("/timeframes/save").flashAttr("timeframe", timeframe))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/timeframes"));

		verify(timeframeService).create(timeframe);
	}

	@Test
	public void givenTimeframe_whenSave_thenTimeframeIsUpdating() throws Exception {
		Timeframe timeframe = buildTimeframe();

		mockMvc.perform(post("/timeframes/save").flashAttr("timeframe", timeframe))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/timeframes"));

		verify(timeframeService).update(timeframe);
	}

	@Test
	public void givenTimeframe_whenDelete_thenTimeframeIsDeleting() throws Exception {
		Timeframe timeframe = buildTimeframe();

		mockMvc.perform(post("/timeframes/{id}/delete", 1))
				.andExpect(status().isFound())
				.andExpect(redirectedUrl("/timeframes"));

		verify(timeframeService).deleteById(timeframe.getId());
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
