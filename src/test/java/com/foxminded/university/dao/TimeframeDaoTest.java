package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.foxminded.university.model.Timeframe;

@DataJpaTest
public class TimeframeDaoTest {

	@Autowired
	TestEntityManager entityManager;
	@Autowired
	TimeframeDao timeframeDao;

	@Test
	public void givenName_whenFindByName_thenGetRightCourse() {
		Optional<Timeframe> expected = Optional.of(entityManager.find(Timeframe.class, 1L));

		Optional<Timeframe> actual = timeframeDao.findBySequence(1);

		assertEquals(expected, actual);
	}
}
