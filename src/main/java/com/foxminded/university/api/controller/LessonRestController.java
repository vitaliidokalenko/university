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
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.TeacherService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonRestController {

	private final LessonService lessonService;
	private final TeacherService teacherService;

	public LessonRestController(LessonService lessonService, TeacherService teacherService) {
		this.lessonService = lessonService;
		this.teacherService = teacherService;
	}

	@GetMapping
	public Page<Lesson> getAll(Pageable pageable) {
		return lessonService.getAllPage(pageable);
	}

	@GetMapping("/{id}")
	public Lesson getById(@PathVariable Long id) {
		return lessonService.findById(id);
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody Lesson lesson) {
		lessonService.create(lesson);
		return ResponseEntity.created(linkTo(methodOn(LessonRestController.class).getById(lesson.getId())).toUri())
				.build();
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @Valid @RequestBody Lesson lesson) {
		lessonService.update(lesson);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		lessonService.deleteById(id);
	}

	@ApiOperation("Replace teacher by defined substitutes between dates")
	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/teacher")
	public void replaceTeacher(@RequestParam Long teacherId,
			@RequestParam(value = "substituteTeacherId", required = false) List<Long> substituteTeacherIds,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		Teacher teacher = teacherService.findById(teacherId);
		lessonService.replaceTeacherByDateBetween(teacher, startDate, endDate, substituteTeacherIds);
	}
}
