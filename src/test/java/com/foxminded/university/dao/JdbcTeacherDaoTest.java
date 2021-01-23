package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.jdbc.JdbcTeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Teacher;

@SpringJUnitConfig(TestAppConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcTeacherDaoTest {

	private static final String TEACHERS_TABLE_NAME = "teachers";
	private static final String TEACHERS_COURSES_TABLE_NAME = "teachers_courses";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcTeacherDao teacherDao;

	@Test
	@Sql("/dataCourses.sql")
	public void givenTeacher_whenCreate_thenTeacherIsAddedToTable() {
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Teacher teacher = new Teacher("Victor", "Doncov");
		teacher.setId(1L);
		teacher.setBirthDate(LocalDate.parse("1991-01-01"));
		teacher.setGender(Gender.MALE);
		teacher.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));
		int expectedRows = countRowsInTable(jdbcTemplate, TEACHERS_TABLE_NAME) + 1;

		teacherDao.create(teacher);

		int actualRows = countRowsInTable(jdbcTemplate, TEACHERS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataCourses.sql")
	public void givenTeacherWithCourses_whenCreate_thenRightDataIsAddedToTeachersCoursesTable() {
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Teacher teacher = new Teacher("Victor", "Doncov");
		teacher.setId(1L);
		teacher.setBirthDate(LocalDate.parse("1991-01-01"));
		teacher.setGender(Gender.MALE);
		teacher.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));
		int expectedRows = countRowsInTable(jdbcTemplate, TEACHERS_COURSES_TABLE_NAME) + 3;

		teacherDao.create(teacher);

		int actualRows = countRowsInTable(jdbcTemplate, TEACHERS_COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataTeachers.sql")
	public void givenTeachers_whenGetAll_thenGetRightListOfTeachers() {
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Teacher teacher1 = new Teacher("Victor", "Doncov");
		teacher1.setId(1L);
		teacher1.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));
		teacher1.setBirthDate(LocalDate.parse("1991-01-01"));
		teacher1.setGender(Gender.MALE);
		Teacher teacher2 = new Teacher("Aleksandra", "Ivanova");
		teacher2.setId(2L);
		teacher2.setBirthDate(LocalDate.parse("1992-02-02"));
		teacher2.setGender(Gender.FEMALE);
		Teacher teacher3 = new Teacher("Anatoly", "Sviridov");
		teacher3.setId(3L);
		teacher3.setBirthDate(LocalDate.parse("1993-03-03"));
		teacher3.setGender(Gender.MALE);
		List<Teacher> expected = Arrays.asList(teacher1, teacher2, teacher3);

		List<Teacher> actual = teacherDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataTeachers.sql")
	public void givenId_whenFindById_thenGetRightTeacher() {
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Teacher expected = new Teacher("Victor", "Doncov");
		expected.setId(1L);
		expected.setBirthDate(LocalDate.parse("1991-01-01"));
		expected.setGender(Gender.MALE);
		expected.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));

		Teacher actual = teacherDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataTeachers.sql")
	public void givenUpdatedFields_whenUpdate_thenTeachersTableIsUpdated() {
		Teacher teacher = new Teacher("Oleg", "Gricina");
		teacher.setId(1L);
		teacher.setBirthDate(LocalDate.parse("1994-04-04"));
		teacher.setGender(Gender.MALE);
		int expectedRows = countRowsInTableWhere(jdbcTemplate, TEACHERS_TABLE_NAME, "surname = 'Gricina'") + 1;

		teacherDao.update(teacher);

		int actualRows = countRowsInTableWhere(jdbcTemplate, TEACHERS_TABLE_NAME, "surname = 'Gricina'");
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataTeachers.sql")
	public void givenUpdatedCourses_whenUpdate_thenTeachersCoursesTableIsUpdated() {
		Course course = new Course("Art");
		course.setId(4L);
		Set<Course> courses = new HashSet<>();
		courses.add(course);
		Teacher teacher = new Teacher("Oleg", "Gricina");
		teacher.setId(1L);
		teacher.setBirthDate(LocalDate.parse("1994-04-04"));
		teacher.setGender(Gender.MALE);
		teacher.setCourses(courses);
		int expectedRows = countRowsInTable(jdbcTemplate, TEACHERS_COURSES_TABLE_NAME) - 2;

		teacherDao.update(teacher);

		int actualRows = countRowsInTable(jdbcTemplate, TEACHERS_COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataTeachers.sql")
	public void givenTeacherId_whenDeleteById_thenTeacherIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, TEACHERS_TABLE_NAME) - 1;

		teacherDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, TEACHERS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/data.sql")
	public void givenCourseId_whenGetTeachersByCourseId_thenGetRightListOfTeachers() {
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Teacher teacher1 = new Teacher("Victor", "Doncov");
		teacher1.setId(1L);
		teacher1.setBirthDate(LocalDate.parse("1991-01-01"));
		teacher1.setGender(Gender.MALE);
		teacher1.setCourses(new HashSet<>(Arrays.asList(course1, course2)));
		Teacher teacher2 = new Teacher("Aleksandra", "Ivanova");
		teacher2.setId(2L);
		teacher2.setBirthDate(LocalDate.parse("1992-02-02"));
		teacher2.setGender(Gender.FEMALE);
		teacher2.setCourses(new HashSet<>(Arrays.asList(course2, course3)));
		List<Teacher> expected = Arrays.asList(teacher1, teacher2);

		List<Teacher> actual = teacherDao.getTeachersByCourseId(2L);

		assertEquals(expected, actual);
	}
}
