package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

@DataJpaTest
public class TeacherDaoTest {

	@Autowired
	TestEntityManager entityManager;
	@Autowired
	TeacherDao teacherDao;

	@Test
	public void givenCourseId_whenGetByCourseId_thenGetRightListOfTeachers() {
		Teacher teacher = entityManager.find(Teacher.class, 1L);
		teacher.setCourses(Set.of(entityManager.find(Course.class, 1L)));
		List<Teacher> expected = List.of(teacher);

		List<Teacher> actual = teacherDao.getByCoursesId(1L);

		assertEquals(expected, actual);
	}
}
