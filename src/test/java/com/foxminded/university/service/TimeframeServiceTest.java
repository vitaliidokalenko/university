package com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.IncorrectDurationException;
import com.foxminded.university.service.exception.IncorrectTimelineException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueSequenceException;

@ExtendWith(MockitoExtension.class)
public class TimeframeServiceTest {

	@Mock
	private TimeframeDao timeframeDao;
	@Mock
	private UniversityConfigProperties properties;

	@InjectMocks
	private TimeframeService timeframeService;

	@Test
	public void givenTimeframe_whenCreate_thenTimeframeIsCreating() {
		Timeframe timeframe = buildTimeframe();
		Duration duration = Duration.parse("PT1H20M");
		when(properties.getLessonDuration()).thenReturn(duration);

		timeframeService.create(timeframe);

		verify(timeframeDao).save(timeframe);
	}

	@Test
	public void givenSequenceLessThanOne_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setSequence(-1);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> timeframeService.create(timeframe));
		assertEquals("Timeframe sequence less than 1", exception.getMessage());
	}

	@Test
	public void givenStartTimeIsNull_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setStartTime(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> timeframeService.create(timeframe));
		assertEquals("Empty timeframe start time", exception.getMessage());
	}

	@Test
	public void givenEndTimeIsNull_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setEndTime(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> timeframeService.create(timeframe));
		assertEquals("Empty timeframe end time", exception.getMessage());
	}

	@Test
	public void givenStartTimeIsAfterEndTime_whenCreate_thenIncorrectTimelineExceptionThrown() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setStartTime(LocalTime.parse("10:00"));

		Exception exception = assertThrows(IncorrectTimelineException.class, () -> timeframeService.create(timeframe));
		assertEquals("Start time is after end time", exception.getMessage());
	}

	@Test
	public void givenIllegalDuration_whenCreate_thenIncorrectDurationExceptionThrown() {
		Timeframe timeframe = buildTimeframe();
		timeframe.setStartTime(LocalTime.parse("08:01"));
		Duration duration = Duration.parse("PT1H20M");
		when(properties.getLessonDuration()).thenReturn(duration);

		Exception exception = assertThrows(IncorrectDurationException.class, () -> timeframeService.create(timeframe));
		assertEquals(format("Not valid timeframe duration. It must be %smin.", duration.toMinutes()),
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
		List<Timeframe> expected = List.of(buildTimeframe());
		when(timeframeDao.findAll()).thenReturn(expected);

		List<Timeframe> actual = timeframeService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTimeframe_whenUpdate_thenTimeframeIsUpdating() {
		Timeframe timeframe = buildTimeframe();
		Duration duration = Duration.parse("PT1H20M");
		when(properties.getLessonDuration()).thenReturn(duration);

		timeframeService.update(timeframe);

		verify(timeframeDao).save(timeframe);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenTimeframeIsDeleting() {
		Timeframe timeframe = buildTimeframe();
		when(timeframeDao.findById(1L)).thenReturn(Optional.of(timeframe));

		timeframeService.deleteById(1L);

		verify(timeframeDao).delete(timeframe);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenNotFoundEntityExceptionThrown() {
		when(timeframeDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> timeframeService.deleteById(1L));
		assertEquals("Cannot find timeframe by id: 1", exception.getMessage());
	}

	@Test
	public void givenSequenceIsNotUnique_whenCreate_thenNotUniqueSequenceExceptionThrown() {
		Timeframe actual = buildTimeframe();
		Timeframe retrieved = buildTimeframe();
		retrieved.setId(2L);
		Duration duration = Duration.parse("PT1H20M");
		when(properties.getLessonDuration()).thenReturn(duration);
		when(timeframeDao.findBySequence(actual.getSequence())).thenReturn(Optional.of(retrieved));

		Exception exception = assertThrows(NotUniqueSequenceException.class, () -> timeframeService.create(actual));
		assertEquals(format("The timeframe with sequence: %d already exists", actual.getSequence()),
				exception.getMessage());
	}

	@Test
	public void whenGetAllPage_thenGetRightTimeframes() {
		Page<Timeframe> expected = new PageImpl<>(List.of(buildTimeframe()));
		when(timeframeDao.findAll(PageRequest.of(0, 1))).thenReturn(expected);

		Page<Timeframe> actual = timeframeService.getAllPage(PageRequest.of(0, 1));

		assertEquals(expected, actual);
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
