package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;

@SpringJUnitConfig(TestAppConfig.class)
@Transactional
public class HibernateTeacherDaoTest {

	@Autowired
	HibernateTemplate template;
	@Autowired
	HibernateTeacherDao teacherDao;

	@Test
	public void whenGetAll_thenGetRightListOfTeachers() {
		List<Teacher> expected = template.loadAll(Teacher.class);

		List<Teacher> actual = teacherDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenNewTeacher_whenCreate_thenCreated() {
		Teacher expected = Teacher.builder()
				.name("Homer")
				.surname("Simpson")
				.courses(Set.of(template.get(Course.class, 1L)))
				.gender(Gender.MALE)
				.birthDate(LocalDate.parse("2001-01-01"))
				.build();

		teacherDao.create(expected);

		Teacher actual = template.get(Teacher.class, expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenFindById_thenGetRightTeacher() {
		Optional<Teacher> expected = Optional.of(template.get(Teacher.class, 1L));

		Optional<Teacher> actual = teacherDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenWrongId_whenFindById_thenGetEmptyOptional() {
		Optional<Teacher> expected = Optional.empty();

		Optional<Teacher> actual = teacherDao.findById(10L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenUpdatedFields_whenUpdate_thenTeacherUpdated() {
		String expectedName = "Updated Name";
		Teacher teacher = template.get(Teacher.class, 1L);
		teacher.setName(expectedName);

		teacherDao.update(teacher);

		assertEquals(expectedName, template.get(Teacher.class, 1L).getName());
	}

	@Test
	public void givenUpdatedCourses_whenUpdate_thenTeachersCoursesUpdated() {
		Teacher teacher = template.get(Teacher.class, 1L);
		teacher.getCourses().clear();

		teacherDao.update(teacher);

		assertTrue(template.get(Teacher.class, 1L).getCourses().isEmpty());
	}

	@Test
	public void givenTeacher_whenDelete_thenDeleted() {
		Teacher teacher = template.get(Teacher.class, 4L);

		teacherDao.delete(teacher);

		assertNull(template.get(Teacher.class, 4L));
	}

	@Test
	public void whenCount_thenGetRightAmountOfTeachers() {
		long expected = template.loadAll(Teacher.class).size();

		long actual = teacherDao.count();

		assertEquals(expected, actual);
	}

	@Test
	public void givenPageSize_whenGetAllPage_thenGetRightTeachers() {
		List<Teacher> expected = template.loadAll(Teacher.class).subList(0, 2);
		int pageSize = 2;

		Page<Teacher> actual = teacherDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}

	@Test
	public void givenCourseId_whenGetByCourseId_thenGetRightListOfTeachers() {
		Teacher teacher = template.get(Teacher.class, 1L);
		teacher.setCourses(Set.of(template.get(Course.class, 1L)));
		List<Teacher> expected = List.of(teacher);

		List<Teacher> actual = teacherDao.getByCourseId(1L);

		assertEquals(expected, actual);
	}
}
