package com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class TimeframeServiceTest {

	private static final long DURATION = 80;

	@Mock
	private TimeframeDao timeframeDao;

	@InjectMocks
	private TimeframeService timeframeService;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(timeframeService, "duration", DURATION);
	}

	@Test
	public void givenTimeframe_whenCreate_thenTimeframeIsCreating() {
		Timeframe timeframe = buildTimeframe();

		timeframeService.create(timeframe);

		verify(timeframeDao).create(timeframe);
	}

	@Test
	public void givenSequanceLessThanOne_whenCreate_thenThrowException() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setSequance(-1);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> timeframeService.create(timeframe));
		assertEquals("Sequance of the timeframe is less than 1", exception.getMessage());
	}

	@Test
	public void givenStartTimeIsNull_whenCreate_thenThrowException() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setStartTime(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> timeframeService.create(timeframe));
		assertEquals("Start time of the timeframe is absent", exception.getMessage());
	}

	@Test
	public void givenEndTimeIsNull_whenCreate_thenThrowException() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setEndTime(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> timeframeService.create(timeframe));
		assertEquals("End time of the timeframe is absent", exception.getMessage());
	}

	@Test
	public void givenStartTimeIsAfterEndTime_whenCreate_thenThrowException() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setStartTime(LocalTime.parse("10:00"));

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> timeframeService.create(timeframe));
		assertEquals("Start time of the timeframe is after end time", exception.getMessage());
	}

	@Test
	public void givenIllegalDuration_whenCreate_thenThrowException() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setStartTime(LocalTime.parse("08:01"));

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> timeframeService.create(timeframe));
		assertEquals(format("Duration of the timeframe is not valid. It should be %dmin.", DURATION),
				exception.getMessage());
	}

	@Test
	public void givenId_whenFindById_thenGetRightTimeframe() {
		Optional<Timeframe> expected = Optional.of(buildTimeframe());
		when(timeframeDao.findById(1L)).thenReturn(expected);

		Optional<Timeframe> actual = timeframeService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfTimeframes() {
		List<Timeframe> expected = Arrays.asList(buildTimeframe());
		when(timeframeDao.getAll()).thenReturn(expected);

		List<Timeframe> actual = timeframeService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTimeframe_whenUpdate_thenTimeframeIsUpdating() {
		Timeframe timeframe = buildTimeframe();

		timeframeService.update(timeframe);

		verify(timeframeDao).update(timeframe);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenTimeframeIsDeleting() {
		when(timeframeDao.findById(1L)).thenReturn(Optional.of(buildTimeframe()));

		timeframeService.deleteById(1L);

		verify(timeframeDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenThrowException() {
		when(timeframeDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> timeframeService.deleteById(1L));
		assertEquals("There is nothing to delete. Timeframe with id: 1 is absent", exception.getMessage());
	}

	private Timeframe buildTimeframe() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequance(1);
		timeframe.setStartTime(LocalTime.parse("08:00"));
		timeframe.setEndTime(LocalTime.parse("09:20"));
		return timeframe;
	}
}
