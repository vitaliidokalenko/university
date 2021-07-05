package com.foxminded.university.api.controller;

import static java.lang.String.format;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.university.model.Course;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@RestController
@RequestMapping("/api/v1")
public class CourseRestController {

	private final CourseService courseService;

	public CourseRestController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping("/courses")
	public Page<Course> getAll(Pageable pageable) {
		return courseService.getAllPage(pageable);
	}

	@GetMapping("/courses/{id}")
	public ResponseEntity<Course> getById(@PathVariable Long id) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find course by id: %d", id)));
		return ResponseEntity.ok(course);
	}

	@PostMapping("/courses")
	public ResponseEntity<Object> create(@Valid @RequestBody Course course) {
		courseService.create(course);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/courses/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody Course course) {
		courseService.update(course);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/courses/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		courseService.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
