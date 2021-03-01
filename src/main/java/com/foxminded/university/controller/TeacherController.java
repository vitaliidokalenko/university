package com.foxminded.university.controller;

import static java.lang.String.format;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private final TeacherService teacherService;

	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("teachers", teacherService.getAll());
		return "teacher/teachers";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable("id") Long id, Model model) {
		Optional<Teacher> teacher = teacherService.findById(id);
		if (!teacher.isPresent()) {
			throw new NotFoundEntityException(format("Cannot find teacher by id: %d", id));
		}
		model.addAttribute("teacher", teacher.get());
		return "teacher/teacher";
	}
}
