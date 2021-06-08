package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@DataJpaTest
public class StudentDaoTest {

	@Autowired
	TestEntityManager entityManager;
	@Autowired
	StudentDao studentDao;

	@Test
	public void givenCourseId_whenGetByCourseId_thenGetRightListOfStudents() {
		List<Student> expected = List.of(entityManager.find(Student.class, 1L));

		List<Student> actual = studentDao.getByCoursesId(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroup_whenGetByGroup_thenGetRightListOfStudents() {
		Group group = entityManager.find(Group.class, 1L);
		List<Student> expected = List.of(entityManager.find(Student.class, 1L));

		List<Student> actual = studentDao.getByGroup(group);

		assertEquals(expected, actual);
	}
}
