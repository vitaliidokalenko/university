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
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@RestController
@RequestMapping("/api/v1")
public class LessonRestController {

	private final LessonService lessonService;
	private final TeacherService teacherService;

	public LessonRestController(LessonService lessonService, TeacherService teacherService) {
		this.lessonService = lessonService;
		this.teacherService = teacherService;
	}

	@GetMapping("/lessons")
	public Page<Lesson> getAll(Pageable pageable) {
		return lessonService.getAllPage(pageable);
	}

	@GetMapping("/lessons/{id}")
	public ResponseEntity<Lesson> getById(@PathVariable Long id) {
		Lesson lesson = lessonService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find lesson by id: %d", id)));
		return ResponseEntity.ok(lesson);
	}

	@PostMapping("/lessons")
	public ResponseEntity<Object> create(@Valid @RequestBody Lesson lesson) {
		lessonService.create(lesson);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/lessons/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody Lesson lesson) {
		lessonService.update(lesson);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/lessons/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		lessonService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/lessons/teacher")
	public ResponseEntity<Object> replaceTeacher(@RequestParam Long teacherId,
			@RequestParam(value = "substituteTeacherId", required = false) List<Long> substituteTeacherIds,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		Teacher teacher = teacherService.findById(teacherId)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find teacher by id: %d", teacherId)));
		lessonService.replaceTeacherByDateBetween(teacher, startDate, endDate, substituteTeacherIds);
		return ResponseEntity.ok().build();
	}
}
