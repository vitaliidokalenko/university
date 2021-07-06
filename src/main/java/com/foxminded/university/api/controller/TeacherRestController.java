package com.foxminded.university.api.controller;

import static java.lang.String.format;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
import com.foxminded.university.service.exception.NotFoundEntityException;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherRestController {

	private final TeacherService teacherService;
	private final LessonService lessonService;

	public TeacherRestController(TeacherService teacherService, LessonService lessonService) {
		super();
		this.teacherService = teacherService;
		this.lessonService = lessonService;
	}

	@GetMapping
	public Page<Teacher> getAll(Pageable pageable) {
		return teacherService.getAllPage(pageable);
	}

	@GetMapping("/{id}")
	public Teacher getById(@PathVariable Long id) {
		return teacherService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find teacher by id: %d", id)));
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody Teacher teacher) {
		teacherService.create(teacher);
		return ResponseEntity.created(linkTo(methodOn(TeacherRestController.class).getById(teacher.getId())).toUri())
				.build();
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @Valid @RequestBody Teacher teacher) {
		teacherService.update(teacher);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		teacherService.deleteById(id);
	}

	@GetMapping("/{id}/timetable")
	public List<Lesson> getTimetable(@PathVariable Long id,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		Teacher teacher = teacherService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find teacher by id: %d", id)));
		return lessonService.getByTeacherAndDateBetween(teacher, startDate, endDate);
	}

	@GetMapping("/{id}/substitutes")
	public Set<Teacher> getSubstitutes(@PathVariable Long id) {
		Teacher teacher = teacherService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find teacher by id: %d", id)));
		return teacherService.getSubstituteTeachers(teacher);
	}
}
