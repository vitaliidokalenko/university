package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.jdbc.JdbcStudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Student;

@SpringJUnitConfig(TestAppConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcStudentDaoTest {

	private static final String STUDENTS_COURSES_TABLE_NAME = "students_courses";
	private static final String STUDENTS_TABLE_NAME = "students";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcStudentDao studentDao;

	@Test
	@Sql("/dataStudentRelations.sql")
	public void givenStudent_whenCreate_thenStudentIsAddedToTable() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Student student = new Student("Anna", "Dvorecka");
		student.setGroup(group);
		student.setBirthDate(LocalDate.parse("2001-01-01"));
		student.setGender(Gender.FEMALE);
		int expectedRows = countRowsInTable(jdbcTemplate, STUDENTS_TABLE_NAME) + 1;

		studentDao.create(student);

		int actualRows = countRowsInTable(jdbcTemplate, STUDENTS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataStudentRelations.sql")
	public void givenStudentWithCourses_whenCreate_thenRightDataIsAddedToStudentsCoursesTable() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Student student = new Student("Anna", "Dvorecka");
		student.setGroup(group);
		student.setBirthDate(LocalDate.parse("2001-01-01"));
		student.setGender(Gender.FEMALE);
		student.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));
		int expectedRows = countRowsInTable(jdbcTemplate, STUDENTS_COURSES_TABLE_NAME) + 3;

		studentDao.create(student);

		int actualRows = countRowsInTable(jdbcTemplate, STUDENTS_COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	public void givenStudentGroupIsNull_whenCreate_thenStudentIsAddedToTable() {
		Student student = new Student("Anna", "Dvorecka");
		student.setGroup(null);
		student.setBirthDate(LocalDate.parse("2001-01-01"));
		student.setGender(Gender.FEMALE);
		int expectedRows = countRowsInTable(jdbcTemplate, STUDENTS_TABLE_NAME) + 1;

		studentDao.create(student);

		int actualRows = countRowsInTable(jdbcTemplate, STUDENTS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataStudents.sql")
	public void givenStudents_whenGetAll_thenGetRightListOfStudents() {
		Group group1 = new Group("AA-11");
		group1.setId(1L);
		Group group2 = new Group("BB-22");
		group2.setId(2L);
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Student student1 = new Student("Anna", "Dvorecka");
		student1.setId(1L);
		student1.setGroup(group1);
		student1.setBirthDate(LocalDate.parse("2001-01-01"));
		student1.setGender(Gender.FEMALE);
		student1.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));
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
	@Sql("/dataStudents.sql")
	public void givenId_whenFindById_thenGetRightStudent() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Student expected = new Student("Anna", "Dvorecka");
		expected.setId(1L);
		expected.setGroup(group);
		expected.setBirthDate(LocalDate.parse("2001-01-01"));
		expected.setGender(Gender.FEMALE);
		expected.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));

		Student actual = studentDao.findById(1L).orElse(null);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataStudents.sql")
	public void givenUpdatedFields_whenUpdate_thenStudentsTableIsUpdated() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Student student = new Student("Andrey", "Skorochod");
		student.setId(1L);
		student.setGroup(group);
		student.setBirthDate(LocalDate.parse("2011-01-01"));
		student.setGender(Gender.MALE);
		student.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));
		int expectedRows = countRowsInTableWhere(jdbcTemplate, STUDENTS_TABLE_NAME, "birth_date = '2011-01-01'") + 1;

		studentDao.update(student);

		int actualRows = countRowsInTableWhere(jdbcTemplate, STUDENTS_TABLE_NAME, "birth_date = '2011-01-01'");
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataStudents.sql")
	public void givenUpdatedCourses_whenUpdate_thenStudentsCoursesTableIsUpdated() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Course course = new Course("Art");
		course.setId(4L);
		Student student = new Student("Andrey", "Skorochod");
		student.setId(1L);
		student.setGroup(group);
		student.setBirthDate(LocalDate.parse("2011-01-01"));
		student.setGender(Gender.MALE);
		student.setCourses(new HashSet<>(Arrays.asList(course)));
		int expectedRows = countRowsInTable(jdbcTemplate, STUDENTS_COURSES_TABLE_NAME) - 2;

		studentDao.update(student);

		int actualRows = countRowsInTable(jdbcTemplate, STUDENTS_COURSES_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataStudents.sql")
	public void givenStudentId_whenDeleteById_thenStudentIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, STUDENTS_TABLE_NAME) - 1;

		studentDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, STUDENTS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataStudents.sql")
	public void givenGroup_whenGetByGroup_thenGetRightListOfStudents() {
		Group group = new Group("AA-11");
		group.setId(1L);
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Student student = new Student("Anna", "Dvorecka");
		student.setId(1L);
		student.setGroup(group);
		student.setBirthDate(LocalDate.parse("2001-01-01"));
		student.setGender(Gender.FEMALE);
		student.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));
		List<Student> expected = Arrays.asList(student);

		List<Student> actual = studentDao.getByGroup(group);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/data.sql")
	public void givenCourseId_whenGetByCourseId_thenGetRightListOfStudents() {
		Group group1 = new Group("AA-11");
		group1.setId(1L);
		Group group2 = new Group("BB-22");
		group2.setId(2L);
		Course course1 = new Course("Law");
		course1.setId(1L);
		course1.setRooms(new HashSet<>(Arrays.asList(Room.builder().id(2L).name("B222").capacity(30).build(),
				Room.builder().id(1L).name("A111").capacity(30).build())));
		Course course2 = new Course("Biology");
		course2.setId(2L);
		course2.setRooms(new HashSet<>(Arrays.asList(Room.builder().id(3L).name("C333").capacity(30).build(),
				Room.builder().id(2L).name("B222").capacity(30).build())));
		Course course3 = new Course("Music");
		course3.setId(3L);
		Student student1 = new Student("Anna", "Dvorecka");
		student1.setId(1L);
		student1.setGroup(group1);
		student1.setBirthDate(LocalDate.parse("2001-01-01"));
		student1.setGender(Gender.FEMALE);
		student1.setCourses(new HashSet<>(Arrays.asList(course1, course2)));
		Student student2 = new Student("Sergii", "Koklush");
		student2.setId(2L);
		student2.setGroup(group2);
		student2.setBirthDate(LocalDate.parse("2002-02-02"));
		student2.setGender(Gender.MALE);
		student2.setCourses(new HashSet<>(Arrays.asList(course2, course3)));
		List<Student> expected = Arrays.asList(student1, student2);

		List<Student> actual = studentDao.getByCourseId(2L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataStudents.sql")
	public void whenCount_thenGetRightAmountOfStudents() {
		int expected = countRowsInTable(jdbcTemplate, STUDENTS_TABLE_NAME);

		int actual = studentDao.count();

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataStudents.sql")
	public void givenPageSize_whenGetAllPage_thenGetRightStudents() {
		Group group1 = new Group("AA-11");
		group1.setId(1L);
		Group group2 = new Group("BB-22");
		group2.setId(2L);
		Course course1 = new Course("Law");
		course1.setId(1L);
		Course course2 = new Course("Biology");
		course2.setId(2L);
		Course course3 = new Course("Music");
		course3.setId(3L);
		Student student1 = new Student("Anna", "Dvorecka");
		student1.setId(1L);
		student1.setGroup(group1);
		student1.setBirthDate(LocalDate.parse("2001-01-01"));
		student1.setGender(Gender.FEMALE);
		student1.setCourses(new HashSet<>(Arrays.asList(course1, course2, course3)));
		Student student2 = new Student("Sergii", "Koklush");
		student2.setId(2L);
		student2.setGroup(group2);
		student2.setBirthDate(LocalDate.parse("2002-02-02"));
		student2.setGender(Gender.MALE);
		List<Student> expected = Arrays.asList(student1, student2);
		int pageSize = 2;

		Page<Student> actual = studentDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}
}
