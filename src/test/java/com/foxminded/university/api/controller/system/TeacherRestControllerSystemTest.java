package com.foxminded.university.api.controller.system;

import static java.time.LocalTime.parse;
import static org.hamcrest.CoreMatchers.containsString;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DBRider
public class TeacherRestControllerSystemTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private WebTestClient webClient;

	@BeforeEach
	@DataSet(cleanBefore = true)
	public void setUp() {
	}

	@Test
	@DataSet("teachers.yml")
	public void whenGetAll_thenGetRightTeachers() throws Exception {
		Teacher expected = buildTeacher();

		webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/teachers")
						.queryParam("page", "0")
						.queryParam("size", "1")
						.build())
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.content.[0]id")
				.isEqualTo(expected.getId())
				.jsonPath("$.content.[0]name")
				.isEqualTo(expected.getName());
	}

	@Test
	@DataSet("teachers.yml")
	public void givenId_whenGetById_thenGetRightTeacher() throws Exception {
		Teacher expected = buildTeacher();

		webClient.get()
				.uri("/api/v1/teachers/{id}", 1)
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectBody()
				.json(mapper.writeValueAsString(expected));
	}

	@Test
	@DataSet("teachersRelations.yml")
	@ExpectedDataSet("expectedTeachersCreate.yml")
	public void givenNewTeacher_whenCreate_thenTeacherIsCreated() {
		Teacher teacher = buildTeacher();
		teacher.setId(null);

		webClient.post()
				.uri("/api/v1/teachers")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(teacher)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.value("Location", containsString("/api/v1/teachers/1"));
	}

	@Test
	@DataSet("teachers.yml")
	@ExpectedDataSet("expectedTeachersUpdate.yml")
	public void givenTeacher_whenUpdate_thenTeacherIsUpdated() {
		Teacher teacher = buildTeacher();
		teacher.setName("Vitalii");

		webClient.put()
				.uri("/api/v1/teachers/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(teacher)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	@DataSet("teachers.yml")
	@ExpectedDataSet("expectedTeachersDelete.yml")
	public void givenId_whenDelete_thenTeacherIsDeleted() {
		webClient.delete()
				.uri("/api/v1/teachers/{id}", 4)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	@Test
	@DataSet("lessons.yml")
	public void givenDates_whenGetTimetable_thenGetRightLessons() throws Exception {
		List<Lesson> expected = List.of(buildLesson());

		webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/teachers/{id}/timetable")
						.queryParam("startDate", "2020-12-12")
						.queryParam("endDate", "2020-12-12")
						.build(1))
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectBody()
				.json(mapper.writeValueAsString(expected));
	}

	@Test
	@DataSet("teachers.yml")
	public void givenId_whenGetSubstitutes_thenGetRightSubstitutesTeachers() throws Exception {
		Teacher substituteTeacher = Teacher.builder()
				.id(2L)
				.name("Aleksandra")
				.surname("Ivanova")
				.email("ivanova@ukr.net")
				.birthDate(LocalDate.parse("1992-02-02"))
				.courses(Set.of(Course.builder().id(2L).name("Biology").build(),
						Course.builder().id(3L).name("Music").build()))
				.gender(Gender.FEMALE)
				.build();
		Set<Teacher> expected = Set.of(substituteTeacher);

		webClient.get()
				.uri("/api/v1/teachers/{id}/substitutes", 1)
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectBody()
				.json(mapper.writeValueAsString(expected));
	}

	private Teacher buildTeacher() {
		return Teacher.builder()
				.id(1L)
				.name("Victor")
				.surname("Doncov")
				.email("doncov@gmail.com")
				.birthDate(LocalDate.parse("1991-01-01"))
				.gender(Gender.MALE)
				.courses(Set.of(Course.builder().id(1L).name("Law").build(),
						Course.builder().id(2L).name("Biology").build()))
				.build();
	}

	private Lesson buildLesson() {
		Room room1 = Room.builder().id(1L).name("A111").capacity(30).build();
		Room room2 = Room.builder().id(2L).name("B222").capacity(30).build();
		Course course1 = Course.builder().id(1L).name("Law").rooms(Set.of(room1, room2)).build();
		Course course2 = Course.builder().id(2L).name("Biology").rooms(Set.of(room1, room2)).build();
		Set<Group> groups = Set.of(Group.builder().id(2L).name("BB-22").build(),
				Group.builder().id(1L).name("AA-11").build());
		Teacher teacher1 = Teacher.builder()
				.id(1L)
				.name("Victor")
				.surname("Doncov")
				.email("doncov@gmail.com")
				.birthDate(LocalDate.parse("1991-01-01"))
				.gender(Gender.MALE)
				.courses(Set.of(course2, course1))
				.build();
		Timeframe timeframe1 = Timeframe.builder()
				.id(1L)
				.sequence(1)
				.startTime(parse("08:00"))
				.endTime(parse("09:20"))
				.build();
		return Lesson.builder()
				.id(1L)
				.course(course1)
				.date(LocalDate.parse("2020-12-12"))
				.groups(groups)
				.room(room1)
				.teacher(teacher1)
				.timeframe(timeframe1)
				.build();
	}
}
