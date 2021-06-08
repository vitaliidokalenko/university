package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.foxminded.university.model.Group;

@DataJpaTest
public class GroupDaoTest {

	@Autowired
	TestEntityManager entityManager;
	@Autowired
	GroupDao groupDao;

	@Test
	public void givenName_whenFindByName_thenGetRightGroup() {
		Optional<Group> expected = Optional.of(entityManager.find(Group.class, 1L));

		Optional<Group> actual = groupDao.findByName("AA-11");

		assertEquals(expected, actual);
	}
}
