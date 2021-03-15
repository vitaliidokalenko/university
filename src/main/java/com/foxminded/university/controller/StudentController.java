package com.foxminded.university.controller;

import static java.lang.String.format;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public String getAll(Pageable pageable, Model model) {
		Page<Student> studentsPage = studentService.getAllPage(pageable);
		model.addAttribute("studentsPage", studentsPage);
		return "student/students";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Student student = studentService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find student by id: %d", id)));
		model.addAttribute("student", student);
		return "student/student";
	}
}
