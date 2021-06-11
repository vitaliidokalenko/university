package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.foxminded.university.model.Course;

@DataJpaTest
public class CourseDaoTest {

	@Autowired
	TestEntityManager entityManager;
	@Autowired
	CourseDao courseDao;

	@Test
	public void givenName_whenFindByName_thenGetRightCourse() {
		Optional<Course> expected = Optional.of(entityManager.find(Course.class, 1L));

		Optional<Course> actual = courseDao.findByName("Law");

		assertEquals(expected, actual);
	}
}