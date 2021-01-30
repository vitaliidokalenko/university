package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Timeframe;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class TimeframeServiceTest {

	@Mock
	private TimeframeDao timeframeDao;

	@InjectMocks
	private TimeframeService timeframeService;

	@Test
	public void givenTimeframe_whenCreate_thenTimeframeIsCreating() {
		Timeframe timeframe = buildTimeframe();

		timeframeService.create(timeframe);

		verify(timeframeDao).create(timeframe);
	}

	@Test
	public void givenSequanceLessThanOne_whenCreate_thenTimeframeIsNotCreating() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setSequance(-1);

		timeframeService.create(timeframe);

		verify(timeframeDao, never()).create(timeframe);
	}

	@Test
	public void givenStartTimeIsNull_whenCreate_thenTimeframeIsNotCreating() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setStartTime(null);

		timeframeService.create(timeframe);

		verify(timeframeDao, never()).create(timeframe);
	}

	@Test
	public void givenEndTimeIsNull_whenCreate_thenTimeframeIsNotCreating() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setEndTime(null);

		timeframeService.create(timeframe);

		verify(timeframeDao, never()).create(timeframe);
	}

	@Test
	public void givenStartTimeIsAfterEndTime_whenCreate_thenTimeframeIsNotCreating() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setStartTime(LocalTime.parse("10:00"));

		timeframeService.create(timeframe);

		verify(timeframeDao, never()).create(timeframe);
	}

	@Test
	public void givenId_whenFindById_thenGetTimeframe() {
		Optional<Timeframe> expected = Optional.of(buildTimeframe());
		when(timeframeDao.findById(1L)).thenReturn(expected);

		Optional<Timeframe> actual = timeframeService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetListOfAllTimeframes() {
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
	public void givenEntityIsNotPresent_whenDeleteById_thenTimeframeIsNotDeleting() {
		when(timeframeDao.findById(1L)).thenReturn(Optional.empty());

		timeframeService.deleteById(1L);

		verify(timeframeDao, never()).deleteById(1L);
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
