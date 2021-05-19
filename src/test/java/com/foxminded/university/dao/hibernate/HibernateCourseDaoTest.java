package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.foxminded.university.model.Course;

@SpringJUnitConfig(TestAppConfig.class)
@Transactional
public class HibernateCourseDaoTest {

	@Autowired
	HibernateTemplate template;
	@Autowired
	HibernateCourseDao courseDao;

	@Test
	public void whenGetAll_thenGetRightListOfCourses() {
		List<Course> expected = template.loadAll(Course.class);

		List<Course> actual = courseDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenNewCourse_whenCreate_thenCreated() {
		Course expected = Course.builder().name("Architecture").description("some description").build();

		courseDao.create(expected);

		Course actual = template.get(Course.class, expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenFindById_thenGetRightCourse() {
		Optional<Course> expected = Optional.of(template.get(Course.class, 1L));

		Optional<Course> actual = courseDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenWrongId_whenFindById_thenGetEmptyOptional() {
		Optional<Course> expected = Optional.empty();

		Optional<Course> actual = courseDao.findById(10L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenUpdatedFields_whenUpdate_thenCourseUpdated() {
		String expectedName = "Updated Name";
		Course course = template.get(Course.class, 1L);
		course.setName(expectedName);

		courseDao.update(course);

		assertEquals(expectedName, template.get(Course.class, 1L).getName());
	}

	@Test
	public void givenUpdatedRooms_whenUpdate_thenCoursesRoomsUpdated() {
		Course course = template.get(Course.class, 1L);
		course.getRooms().clear();

		courseDao.update(course);

		assertTrue(template.get(Course.class, 1L).getRooms().isEmpty());
	}

	@Test
	public void givenCourse_whenDelete_thenDeleted() {
		Course course = template.get(Course.class, 4L);

		courseDao.delete(course);

		assertNull(template.get(Course.class, 4L));
	}

	@Test
	public void whenCount_thenGetRightAmountOfCourses() {
		long expected = template.loadAll(Course.class).size();

		long actual = courseDao.count();

		assertEquals(expected, actual);
	}

	@Test
	public void givenPageSize_whenGetAllPage_thenGetRightCourses() {
		List<Course> expected = template.loadAll(Course.class).subList(0, 2);
		int pageSize = 2;

		Page<Course> actual = courseDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}

	@Test
	public void givenName_whenFindByName_thenGetRightCourse() {
		Optional<Course> expected = Optional.of(template.get(Course.class, 1L));

		Optional<Course> actual = courseDao.findByName("Law");

		assertEquals(expected, actual);
	}
}