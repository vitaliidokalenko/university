package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.jdbc.JdbcTimeframeDao;
import com.foxminded.university.model.Timeframe;

@SpringJUnitConfig(TestAppConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcTimeframeDaoTest {

	private static final String TIMEFRAMES_TABLE_NAME = "timeframes";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcTimeframeDao timeframeDao;

	@Test
	@Sql("/dataTimeframes.sql")
	public void givenCourses_whenGetAll_thenGetRightListOfCourses() {
		Timeframe timeframe1 = new Timeframe();
		timeframe1.setId(1L);
		timeframe1.setSequence(1);
		timeframe1.setStartTime(LocalTime.parse("08:00"));
		timeframe1.setEndTime(LocalTime.parse("09:20"));
		Timeframe timeframe2 = new Timeframe();
		timeframe2.setId(2L);
		timeframe2.setSequence(2);
		timeframe2.setStartTime(LocalTime.parse("09:40"));
		timeframe2.setEndTime(LocalTime.parse("11:00"));
		Timeframe timeframe3 = new Timeframe();
		timeframe3.setId(3L);
		timeframe3.setSequence(3);
		timeframe3.setStartTime(LocalTime.parse("11:20"));
		timeframe3.setEndTime(LocalTime.parse("12:40"));
		List<Timeframe> expected = Arrays.asList(timeframe1, timeframe2, timeframe3);

		List<Timeframe> actual = timeframeDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenTimeframe_whenCreate_thenTimeframeIsAddedToTable() {
		Timeframe timaframe = new Timeframe();
		timaframe.setId(1L);
		timaframe.setSequence(1);
		timaframe.setStartTime(LocalTime.parse("08:00"));
		timaframe.setEndTime(LocalTime.parse("09:20"));
		int expectedRows = countRowsInTable(jdbcTemplate, TIMEFRAMES_TABLE_NAME) + 1;

		timeframeDao.create(timaframe);

		int actualRows = countRowsInTable(jdbcTemplate, TIMEFRAMES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataTimeframes.sql")
	public void givenId_whenFindById_thenGetRightTimeframe() {
		Timeframe expected = new Timeframe();
		expected.setId(1L);
		expected.setSequence(1);
		expected.setStartTime(LocalTime.parse("08:00"));
		expected.setEndTime(LocalTime.parse("09:20"));

		Timeframe actual = timeframeDao.findById(1L).orElse(null);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataTimeframes.sql")
	public void givenUpdatedFields_whenUpdate_thenTimeframesTableIsUpdated() {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(1L);
		timeframe.setSequence(10);
		timeframe.setStartTime(LocalTime.parse("17:00"));
		timeframe.setEndTime(LocalTime.parse("19:20"));
		int expectedRows = countRowsInTableWhere(jdbcTemplate, TIMEFRAMES_TABLE_NAME, "start_time = '17:00'") + 1;

		timeframeDao.update(timeframe);

		int actualRows = countRowsInTableWhere(jdbcTemplate, TIMEFRAMES_TABLE_NAME, "start_time = '17:00'");
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataTimeframes.sql")
	public void givenTimeframeId_whenDelete_thenTimeframeIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, TIMEFRAMES_TABLE_NAME) - 1;

		timeframeDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, TIMEFRAMES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataTimeframes.sql")
	public void givenSequance_whenFindBySequance_thenGetRightTimeframe() {
		Timeframe expected = new Timeframe();
		expected.setId(1L);
		expected.setSequence(1);
		expected.setStartTime(LocalTime.parse("08:00"));
		expected.setEndTime(LocalTime.parse("09:20"));

		Timeframe actual = timeframeDao.findBySequence(1).orElse(null);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataTimeframes.sql")
	public void whenCount_thenGetRightAmountOfTimeframes() {
		int expected = countRowsInTable(jdbcTemplate, TIMEFRAMES_TABLE_NAME);

		int actual = timeframeDao.count();

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataTimeframes.sql")
	public void givenPageSize_whenGetAllPage_thenGetRightTimeframes() {
		Timeframe timeframe1 = new Timeframe();
		timeframe1.setId(1L);
		timeframe1.setSequence(1);
		timeframe1.setStartTime(LocalTime.parse("08:00"));
		timeframe1.setEndTime(LocalTime.parse("09:20"));
		Timeframe timeframe2 = new Timeframe();
		timeframe2.setId(2L);
		timeframe2.setSequence(2);
		timeframe2.setStartTime(LocalTime.parse("09:40"));
		timeframe2.setEndTime(LocalTime.parse("11:00"));
		List<Timeframe> expected = Arrays.asList(timeframe1, timeframe2);
		int pageSize = 2;

		Page<Timeframe> actual = timeframeDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}
}
