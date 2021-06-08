package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
	public void givenLessonId_whenGetByLessonId_thenGetRightListOfGroups() {
		List<Group> expected = List.of(entityManager.find(Group.class, 1L), entityManager.find(Group.class, 2L));

		List<Group> actual = groupDao.getByLessonId(1L);

		assertEquals(expected, actual);
	}
}
