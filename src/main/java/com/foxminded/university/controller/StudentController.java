package com.foxminded.university.controller;

import static java.lang.String.format;

import java.util.Optional;
import java.util.stream.IntStream;

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
		model.addAttribute("numbers", IntStream.rangeClosed(1, studentsPage.getTotalPages()).toArray());
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
