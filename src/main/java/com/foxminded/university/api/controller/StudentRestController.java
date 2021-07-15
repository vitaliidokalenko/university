package com.foxminded.university.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
public class StudentRestController {

	private final StudentService studentService;
	private final LessonService lessonService;

	public StudentRestController(StudentService studentService, LessonService lessonService) {
		this.studentService = studentService;
		this.lessonService = lessonService;
	}

	@GetMapping
	public Page<Student> getAll(Pageable pageable) {
		return studentService.getAllPage(pageable);
	}

	@GetMapping("/{id}")
	public Student getById(@PathVariable Long id) {
		return studentService.findById(id);
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody Student student) {
		studentService.create(student);
		return ResponseEntity.created(linkTo(methodOn(StudentRestController.class).getById(student.getId())).toUri())
				.build();
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @Valid @RequestBody Student student) {
		studentService.update(student);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		studentService.deleteById(id);
	}

	@GetMapping("/{id}/timetable")
	public List<Lesson> getTimetable(@PathVariable Long id,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		Student student = studentService.findById(id);
		return lessonService.getByGroupAndDateBetween(student.getGroup(), startDate, endDate);
	}
}
