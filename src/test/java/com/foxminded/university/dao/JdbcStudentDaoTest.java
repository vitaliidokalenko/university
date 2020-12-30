package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@SpringJUnitConfig(AppConfig.class)
public class JdbcStudentDaoTest {

	private static final String STUDENTS_COURSES_TABLE_NAME = "students_courses";
	private static final String STUDENTS_TABLE_NAME = "students";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcStudentDao studentDao;

	@Test
	@Sql({ "/schema.sql", "/dataStudents.sql" })
	public void givenStudents_whenGetAll_thenGetRightListOfStudents() {
		Group group1 = new Group("AA-11");
		group1.setId(1L);
		Group group2 = new Group("BB-22");
		group2.setId(2L);
		Student student1 = new Student("Anna", "Dvorecka");
		student1.setId(1L);
		student1.setGroup(group1);
		student1.setBirthDate(LocalDate.parse("2001-01-01"));
		student1.setGender(Gender.FEMALE);
		Student student2 = new Student("Sergii", "Koklush");
		student2.setId(2L);
		student2.setGroup(group2);
		student2.setBirthDate(LocalDate.parse("2002-02-02"));
		student2.setGender(Gender.MALE);
		Student student3 = new Student("Vladislav", "Ostrovski");
		student3.setId(3L);
		student3.setBirthDate(LocalDate.parse("2003-03-03"));
		student3.setGender(Gender.MALE);
		List<Student> expected = Arrays.asList(student1, student2, student3);

		List<Student> actual = studentDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataGroups.sql" })
	public void givenStudent_whenCreate_thenStudentIsAddedToTable() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Student expected = new Student("Anna", "Dvorecka");
		expected.setGroup(group);
		expected.setBirthDate(LocalDate.parse("2001-01-01"));
		expected.setGender(Gender.FEMALE);

		studentDao.create(expected);

		Student actual = studentDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql("/schema.sql")
	public void givenStudentGroupIsNull_whenCreate_thenStudentIsAddedToTable() {
		Student expected = new Student("Anna", "Dvorecka");
		expected.setGroup(null);
		expected.setBirthDate(LocalDate.parse("2001-01-01"));
		expected.setGender(Gender.FEMALE);

		studentDao.create(expected);

		Student actual = studentDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataStudents.sql" })
	public void givenId_whenFindById_thenGetRightStudent() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Student expected = new Student("Anna", "Dvorecka");
		expected.setId(1L);
		expected.setGroup(group);
		expected.setBirthDate(LocalDate.parse("2001-01-01"));
		expected.setGender(Gender.FEMALE);

		Student actual = studentDao.findById(1L);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataStudents.sql" })
	public void givenUpdatedFields_whenUpdate_thenGetRightStudent() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Student expected = new Student("Andrey", "Skorochod");
		expected.setId(1L);
		expected.setGroup(group);
		expected.setBirthDate(LocalDate.parse("2011-01-01"));
		expected.setGender(Gender.MALE);

		studentDao.update(expected);

		Student actual = studentDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataStudents.sql" })
	public void givenStudentId_whenDeleteById_thenStudentIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, STUDENTS_TABLE_NAME) - 1;

		studentDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, STUDENTS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/dataStudents.sql" })
	public void givenGroup_whenGetStudentsByGroup_thenGetRightListOfStudents() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Student student = new Student("Anna", "Dvorecka");
		student.setId(1L);
		student.setGroup(group);
		student.setBirthDate(LocalDate.parse("2001-01-01"));
		student.setGender(Gender.FEMALE);
		List<Student> expected = Arrays.asList(student);

		List<Student> actual = studentDao.getStudentsByGroup(group);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataStudentsCourses.sql" })
	public void givenStudentIdAndCourseId_whenCreateStudentCourse_thenRightDataAddedToTable() {
		int expectedRows = countRowsInTable(jdbcTemplate, STUDENTS_COURSES_TABLE_NAME) + 2;

		studentDao.createStudentCourse(1L, 1L);
		studentDao.createStudentCourse(1L, 2L);

		int actualRows = countRowsInTable(jdbcTemplate, STUDENTS_COURSES_TABLE_NAME);

		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenStudentIdAndCourseId_whenDeleteStudentCourse_thenRightDataDeletedFromTable() {
		int expectedRows = countRowsInTable(jdbcTemplate, STUDENTS_COURSES_TABLE_NAME) - 1;

		studentDao.deleteStudentCourse(1L, 1L);

		int actualRows = countRowsInTable(jdbcTemplate, STUDENTS_COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenCourseId_whenGetStudentsByCourseId_thenGetRightListOfStudents() {
		Group group1 = new Group("AA-11");
		group1.setId(1L);
		Group group2 = new Group("BB-22");
		group2.setId(2L);
		Student student1 = new Student("Anna", "Dvorecka");
		student1.setId(1L);
		student1.setGroup(group1);
		student1.setBirthDate(LocalDate.parse("2001-01-01"));
		student1.setGender(Gender.FEMALE);
		Student student2 = new Student("Sergii", "Koklush");
		student2.setId(2L);
		student2.setGroup(group2);
		student2.setBirthDate(LocalDate.parse("2002-02-02"));
		student2.setGender(Gender.MALE);
		List<Student> expected = Arrays.asList(student1, student2);

		List<Student> actual = studentDao.getStudentsByCourseId(2L);

		assertEquals(expected, actual);
	}

}
