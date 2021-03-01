package com.foxminded.university.controller;

import static java.lang.String.format;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Student;
import com.foxminded.university.service.StudentService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Controller
@RequestMapping("/students")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("students", studentService.getAll());
		return "student/students";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable("id") Long id, Model model) {
		Optional<Student> student = studentService.findById(id);
		if (!student.isPresent()) {
			throw new NotFoundEntityException(format("Cannot find student by id: %d", id));
		}
		model.addAttribute("student", student.get());
		return "student/student";
	}
}
