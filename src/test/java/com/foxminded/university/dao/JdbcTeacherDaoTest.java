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
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;

@SpringJUnitConfig(AppConfig.class)
public class JdbcTeacherDaoTest {

	private static final String TEACHERS_TABLE_NAME = "teachers";
	private static final String TEACHERS_COURSES_TABLE_NAME = "teachers_courses";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcTeacherDao teacherDao;

	@Test
	@Sql({ "/schema.sql", "/dataTeachers.sql" })
	public void givenTeachers_whenGetAll_thenGetRightListOfTeachers() {
		Teacher teacher1 = new Teacher("Victor", "Doncov");
		teacher1.setId(1L);
		teacher1.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher1.setGender(Gender.MALE);
		Teacher teacher2 = new Teacher("Aleksandra", "Ivanova");
		teacher2.setId(2L);
		teacher2.setBirthdate(LocalDate.parse("1992-02-02"));
		teacher2.setGender(Gender.FEMALE);
		Teacher teacher3 = new Teacher("Anatoly", "Sviridov");
		teacher3.setId(3L);
		teacher3.setBirthdate(LocalDate.parse("1993-03-03"));
		teacher3.setGender(Gender.MALE);
		List<Teacher> expected = Arrays.asList(teacher1, teacher2, teacher3);

		List<Teacher> actual = teacherDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/schema.sql")
	public void givenTeacher_whenCreate_thenTeacherIsAddedToTable() {
		Teacher expected = new Teacher("Victor", "Doncov");
		expected.setId(1L);
		expected.setBirthdate(LocalDate.parse("1991-01-01"));
		expected.setGender(Gender.MALE);

		teacherDao.create(expected);

		Teacher actual = teacherDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataTeachers.sql" })
	public void givenId_whenFindById_thenGetRightTeacher() {
		Teacher expected = new Teacher("Victor", "Doncov");
		expected.setId(1L);
		expected.setBirthdate(LocalDate.parse("1991-01-01"));
		expected.setGender(Gender.MALE);

		Teacher actual = teacherDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataTeachers.sql" })
	public void givenUpdatedFields_whenUpdate_thenGetRightTeacher() {
		Teacher expected = new Teacher("Oleg", "Gricina");
		expected.setId(1L);
		expected.setBirthdate(LocalDate.parse("1994-04-04"));
		expected.setGender(Gender.MALE);

		teacherDao.update(expected);

		Teacher actual = teacherDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataTeachers.sql" })
	public void givenTeacherId_whenDeleteById_thenTeacherIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, TEACHERS_TABLE_NAME) - 1;

		teacherDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, TEACHERS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/dataTeachersCourses.sql" })
	public void givenTeacherIdAndCourseId_whenCreateTeacherCourse_thenRightDataAddedToTable() {
		int expectedRows = countRowsInTable(jdbcTemplate, TEACHERS_COURSES_TABLE_NAME) + 2;

		teacherDao.createTeacherCourse(1L, 1L);
		teacherDao.createTeacherCourse(1L, 2L);

		int actualRows = countRowsInTable(jdbcTemplate, TEACHERS_COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenTeacherIdAndCourseId_whenDeleteTeacherCourse_thenRightDataDeletedFromTable() {
		int expectedRows = countRowsInTable(jdbcTemplate, TEACHERS_COURSES_TABLE_NAME) - 1;

		teacherDao.deleteTeacherCourse(1L, 1L);

		int actualRows = countRowsInTable(jdbcTemplate, TEACHERS_COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenCourseId_whenGetTeachersByCourseId_thenGetRightListOfTeachers() {
		Teacher teacher1 = new Teacher("Victor", "Doncov");
		teacher1.setId(1L);
		teacher1.setBirthdate(LocalDate.parse("1991-01-01"));
		teacher1.setGender(Gender.MALE);
		Teacher teacher2 = new Teacher("Aleksandra", "Ivanova");
		teacher2.setId(2L);
		teacher2.setBirthdate(LocalDate.parse("1992-02-02"));
		teacher2.setGender(Gender.FEMALE);
		List<Teacher> expected = Arrays.asList(teacher1, teacher2);

		List<Teacher> actual = teacherDao.getTeachersByCourseId(2L);

		assertEquals(expected, actual);
	}
}
