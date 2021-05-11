package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@SpringJUnitConfig(TestAppConfig.class)
@Transactional
public class HibernateStudentDaoTest {

	@Autowired
	HibernateTemplate template;
	@Autowired
	HibernateStudentDao studentDao;

	@Test
	public void whenGetAll_thenGetRightListOfStudents() {
		List<Student> expected = template.loadAll(Student.class);

		List<Student> actual = studentDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenNewStudent_whenCreate_thenCreated() {
		Student expected = Student.builder()
				.name("Homer")
				.surname("Simpson")
				.group(template.get(Group.class, 1L))
				.courses(Set.of(template.get(Course.class, 1L)))
				.gender(Gender.MALE)
				.birthDate(LocalDate.parse("2001-01-01"))
				.build();

		studentDao.create(expected);

		Student actual = template.get(Student.class, 4L);
		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenFindById_thenGetRightStudent() {
		Optional<Student> expected = Optional.of(template.get(Student.class, 1L));

		Optional<Student> actual = studentDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenWrongId_whenFindById_thenGetEmptyOptional() {
		Optional<Student> expected = Optional.empty();

		Optional<Student> actual = studentDao.findById(10L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenUpdatedFields_whenUpdate_thenStudentUpdated() {
		String expectedName = "Updated Name";
		Student student = template.get(Student.class, 1L);
		student.setName(expectedName);

		studentDao.update(student);

		assertEquals(expectedName, template.get(Student.class, 1L).getName());
	}

	@Test
	public void givenUpdatedCourses_whenUpdate_thenStudentsCoursesUpdated() {
		Student student = template.get(Student.class, 1L);
		student.getCourses().clear();

		studentDao.update(student);

		assertTrue(template.get(Student.class, 1L).getCourses().isEmpty());
	}

	@Test
	public void givenStudent_whenDelete_thenDeleted() {
		Student student = template.get(Student.class, 3L);
		int expectedRows = template.loadAll(Student.class).size() - 1;

		studentDao.delete(student);

		int actualRows = template.loadAll(Student.class).size();
		assertEquals(expectedRows, actualRows);
	}

	@Test
	public void whenCount_thenGetRightAmountOfStudents() {
		long expected = template.loadAll(Student.class).size();

		long actual = studentDao.count();

		assertEquals(expected, actual);
	}

	@Test
	public void givenPageSize_whenGetAllPage_thenGetRightStudents() {
		List<Student> expected = template.loadAll(Student.class).subList(0, 2);
		int pageSize = 2;

		Page<Student> actual = studentDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}

	@Test
	public void givenCourseId_whenGetByCourseId_thenGetRightListOfStudents() {
		List<Student> expected = List.of(template.get(Student.class, 1L));

		List<Student> actual = studentDao.getByCourseId(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroup_whenGetByGroup_thenGetRightListOfStudents() {
		Group group = template.get(Group.class, 1L);
		List<Student> expected = List.of(template.get(Student.class, 1L));

		List<Student> actual = studentDao.getByGroup(group);

		assertEquals(expected, actual);
	}
}
