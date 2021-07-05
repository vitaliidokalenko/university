package com.foxminded.university.api.controller;

import static java.lang.String.format;

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
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.StudentService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@RestController
@RequestMapping("/api/v1")
public class StudentRestController {

	private final StudentService studentService;
	private final LessonService lessonService;

	public StudentRestController(StudentService studentService, LessonService lessonService) {
		this.studentService = studentService;
		this.lessonService = lessonService;
	}

	@GetMapping("/students")
	public Page<Student> getAll(Pageable pageable) {
		return studentService.getAllPage(pageable);
	}

	@GetMapping("/students/{id}")
	public ResponseEntity<Student> getById(@PathVariable Long id) {
		Student student = studentService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find student by id: %d", id)));
		return ResponseEntity.ok(student);
	}

	@PostMapping("/students")
	public ResponseEntity<Object> create(@Valid @RequestBody Student student) {
		studentService.create(student);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/students/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody Student student) {
		studentService.update(student);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/students/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		studentService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/students/{id}/timetable")
	public List<Lesson> getTimetable(@PathVariable Long id,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		Student student = studentService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find student by id: %d", id)));
		return lessonService.getByGroupAndDateBetween(student.getGroup(), startDate, endDate);
	}
}
