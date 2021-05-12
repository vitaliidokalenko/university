package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.model.Timeframe;

@SpringJUnitConfig(TestAppConfig.class)
@Transactional
public class HibernateTimeframeDaoTest {

	@Autowired
	HibernateTemplate template;
	@Autowired
	HibernateTimeframeDao timeframeDao;

	@Test
	public void whenGetAll_thenGetRightListOfTimeframes() {
		List<Timeframe> expected = template.loadAll(Timeframe.class);

		List<Timeframe> actual = timeframeDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenNewTimeframe_whenCreate_thenCreated() {
		Timeframe expected = Timeframe.builder()
				.sequence(5)
				.startTime(LocalTime.parse("14:40"))
				.endTime(LocalTime.parse("16:00"))
				.build();

		timeframeDao.create(expected);

		Timeframe actual = template.get(Timeframe.class, 5L);
		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenFindById_thenGetRightTimeframe() {
		Optional<Timeframe> expected = Optional.of(template.get(Timeframe.class, 1L));

		Optional<Timeframe> actual = timeframeDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenWrongId_whenFindById_thenGetEmptyOptional() {
		Optional<Timeframe> expected = Optional.empty();

		Optional<Timeframe> actual = timeframeDao.findById(10L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenUpdatedFields_whenUpdate_thenTimeframeUpdated() {
		int expectedSequence = 10;
		Timeframe timeframe = template.get(Timeframe.class, 1L);
		timeframe.setSequence(expectedSequence);

		timeframeDao.update(timeframe);

		assertEquals(expectedSequence, template.get(Timeframe.class, 1L).getSequence());
	}

	@Test
	public void givenTimeframe_whenDelete_thenDeleted() {
		Timeframe timeframe = template.get(Timeframe.class, 4L);

		timeframeDao.delete(timeframe);

		assertNull(template.get(Timeframe.class, 4L));
	}

	@Test
	public void whenCount_thenGetRightAmountOfTimeframes() {
		long expected = template.loadAll(Timeframe.class).size();

		long actual = timeframeDao.count();

		assertEquals(expected, actual);
	}

	@Test
	public void givenPageSize_whenGetAllPage_thenGetRightTimeframes() {
		List<Timeframe> expected = template.loadAll(Timeframe.class).subList(0, 2);
		int pageSize = 2;

		Page<Timeframe> actual = timeframeDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}

	@Test
	public void givenName_whenFindByName_thenGetRightCourse() {
		Optional<Timeframe> expected = Optional.of(template.get(Timeframe.class, 1L));

		Optional<Timeframe> actual = timeframeDao.findBySequence(1);

		assertEquals(expected, actual);
	}
}
