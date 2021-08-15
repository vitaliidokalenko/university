package com.foxminded.university.api.controller.system;

import static org.hamcrest.CoreMatchers.containsString;

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
import com.foxminded.university.model.Room;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FlywayTestExtension
@DBRider
public class CourseRestControllerSystemTest {

	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private WebTestClient webClient;

	@Test
	@FlywayTest
	@DataSet("courses.yml")
	public void whenGetAll_thenGetRightCourses() {
		Course expected = buildCourse();

		webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/v1/courses")
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
	@FlywayTest
	@DataSet("courses.yml")
	public void givenId_whenGetById_thenGetRightCourse() throws Exception {
		Course expected = buildCourse();

		webClient.get()
				.uri("/api/v1/courses/{id}", 1)
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
	@DataSet("coursesRelations.yml")
	@ExpectedDataSet("expectedCoursesCreate.yml")
	public void givenNewCourse_whenCreate_thenCourseIsCreated() {
		Course course = Course.builder()
				.name("Geography")
				.rooms(Set.of(Room.builder().id(1L).name("A111").capacity(30).build()))
				.build();

		webClient.post()
				.uri("/api/v1/courses")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(course)
				.exchange()
				.expectStatus()
				.isCreated()
				.expectHeader()
				.value("Location", containsString("/api/v1/courses/1"));
	}

	@Test
	@FlywayTest
	@DataSet("courses.yml")
	@ExpectedDataSet("expectedCoursesUpdate.yml")
	public void givenCourse_whenUpdate_thenCourseIsUpdated() {
		Course course = buildCourse();
		course.setName("Geography");

		webClient.put()
				.uri("/api/v1/courses/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(course)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	@FlywayTest
	@DataSet("courses.yml")
	@ExpectedDataSet("expectedCoursesDelete.yml")
	public void givenId_whenDelete_thenCourseIsDeleted() {
		webClient.delete()
				.uri("/api/v1/courses/{id}", 4)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	private Course buildCourse() {
		return Course.builder()
				.id(1L)
				.name("Law")
				.rooms(Set.of(Room.builder().id(1L).name("A111").capacity(30).build(),
						Room.builder().id(2L).name("B222").capacity(30).build()))
				.build();
	}
}
