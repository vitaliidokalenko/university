package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

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
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);

		timeframeService.create(timeframe);

		verify(timeframeDao).create(timeframe);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Timeframe expected = new Timeframe();
		Long id = 1L;
		expected.setId(id);
		when(timeframeDao.findById(id)).thenReturn(expected);

		Timeframe actual = timeframeService.findById(id);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		List<Timeframe> expected = Arrays.asList(timeframe);
		when(timeframeDao.getAll()).thenReturn(expected);

		List<Timeframe> actual = timeframeService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTimeframe_whenUpdate_thenTimeframeIsUpdating() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);

		timeframeService.update(timeframe);

		verify(timeframeDao).update(timeframe);
	}

	@Test
	public void givenId_whenDeleteById_thenTimefarmeIsDeleting() {
		timeframeService.deleteById(1L);

		verify(timeframeDao).deleteById(1L);
	}
}
