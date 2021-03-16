package com.foxminded.university.controller;

import static java.lang.String.format;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public String getAll(Pageable pageable, Model model) {
		Page<Teacher> teachersPage = teacherService.getAllPage(pageable);
		model.addAttribute("teachersPage", teachersPage);
		return "teacher/teachers";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Teacher teacher = teacherService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find teacher by id: %d", id)));
		model.addAttribute("teacher", teacher);
		return "teacher/teacher";
	}
}
