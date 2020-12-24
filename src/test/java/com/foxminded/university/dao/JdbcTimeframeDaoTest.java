package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.dao.jdbc.JdbcTimeframeDao;
import com.foxminded.university.model.Timeframe;

@SpringJUnitConfig(AppConfig.class)
public class JdbcTimeframeDaoTest {

	private static final String TIMEFRAMES_TABLE_NAME = "timeframes";

	private JdbcTemplate jdbcTemplate;
	private JdbcTimeframeDao timeframeDao;

	@Autowired
	public JdbcTimeframeDaoTest(DataSource dataSource, JdbcTimeframeDao timeframeDao) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.timeframeDao = timeframeDao;
	}

	@Test
	@Sql({ "/schema.sql", "/dataTimeframes.sql" })
	public void givenCourses_whenGetAll_thenGetRightListOfCourses() {
		Timeframe timeframe1 = new Timeframe();
		timeframe1.setId(1L);
		timeframe1.setSequance(1);
		timeframe1.setStartTime(LocalTime.parse("08:00"));
		timeframe1.setEndTime(LocalTime.parse("09:20"));
		Timeframe timeframe2 = new Timeframe();
		timeframe2.setId(2L);
		timeframe2.setSequance(2);
		timeframe2.setStartTime(LocalTime.parse("09:40"));
		timeframe2.setEndTime(LocalTime.parse("11:00"));
		Timeframe timeframe3 = new Timeframe();
		timeframe3.setId(3L);
		timeframe3.setSequance(3);
		timeframe3.setStartTime(LocalTime.parse("11:20"));
		timeframe3.setEndTime(LocalTime.parse("12:40"));
		List<Timeframe> expected = Arrays.asList(timeframe1, timeframe2, timeframe3);

		List<Timeframe> actual = timeframeDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/schema.sql")
	public void givenTimeframe_whenCreate_thenTimeframeIsAddedToTable() {
		Timeframe expected = new Timeframe();
		expected.setId(1L);
		expected.setSequance(1);
		expected.setStartTime(LocalTime.parse("08:00"));
		expected.setEndTime(LocalTime.parse("09:20"));

		timeframeDao.create(expected);

		Timeframe actual = timeframeDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataTimeframes.sql" })
	public void givenId_whenFindById_thenGetRightTimeframe() {
		Timeframe expected = new Timeframe();
		expected.setId(1L);
		expected.setSequance(1);
		expected.setStartTime(LocalTime.parse("08:00"));
		expected.setEndTime(LocalTime.parse("09:20"));

		Timeframe actual = timeframeDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataTimeframes.sql" })
	public void givenUpdatedFields_whenUpdate_thenGetRightTimeframe() {
		Timeframe expected = new Timeframe();
		expected.setId(1L);
		expected.setSequance(10);
		expected.setStartTime(LocalTime.parse("17:00"));
		expected.setEndTime(LocalTime.parse("19:20"));

		timeframeDao.update(expected);

		Timeframe actual = timeframeDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataTimeframes.sql" })
	public void givenTimeframeId_whenDelete_thenTimeframeIsDeleted() {
		int expectedRows = 2;

		timeframeDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, TIMEFRAMES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

}
