package com.foxminded.university.api.controller.system;

import static java.time.LocalTime.parse;
import static org.hamcrest.CoreMatchers.containsString;

import java.time.LocalDate;
import java.util.Set;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FlywayTestExtension
public class LessonRestControllerSystemTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private WebTestClient webClient;

	@Test
	@FlywayTest
	public void whenGetAll_thenGetRightLessons() {
		Lesson expected = buildLesson();

		webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/lessons")
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
				.jsonPath("$.content.[0]date")
				.isEqualTo(expected.getDate().toString());
	}

	@Test
	@FlywayTest
	public void givenId_whenGetById_thenGetRightLesson() throws Exception {
		Lesson expected = buildLesson();

		webClient.get()
				.uri("/api/v1/lessons/{id}", 1)
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
				.expectBody()
				.json(mapper.writeValueAsString(expected));
	}

	@Test
	@FlywayTest
	public void givenNewLesson_whenCreate_thenLessonIsCreated() {
		Lesson lesson = buildLesson();
		lesson.setId(null);
		lesson.setDate(LocalDate.parse("2070-12-15"));

		webClient.post()
				.uri("/api/v1/lessons")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(lesson)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.value("Location", containsString("/api/v1/lessons/5"));
	}

	@Test
	@FlywayTest
	public void givenLesson_whenUpdate_thenLessonIsUpdated() {
		Lesson lesson = buildLesson();
		lesson.setDate(LocalDate.parse("2070-12-12"));

		webClient.put()
				.uri("/api/v1/lessons/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(lesson)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	@FlywayTest
	public void givenId_whenDelete_thenLessonIsDeleted() {
		webClient.delete()
				.uri("/api/v1/lessons/{id}", 3)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	@Test
	@FlywayTest
	public void givenTeacherAndDatesAndSubstituteTeacherId_whenReplaceTeacher_thenTeacherIsReplacing() {
		webClient.put()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/lessons/teacher")
						.queryParam("teacherId", "2")
						.queryParam("startDate", "2040-12-14")
						.queryParam("endDate", "2040-12-14")
						.queryParam("substituteTeacherId", "1")
						.build())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isOk();
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
