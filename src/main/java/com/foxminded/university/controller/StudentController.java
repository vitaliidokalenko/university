package com.foxminded.university.controller;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Controller
@RequestMapping("/students")
public class StudentController {

	private final StudentService studentService;
	private final CourseService courseService;
	private final GroupService groupService;

	public StudentController(StudentService studentService, CourseService courseService, GroupService groupService) {
		this.studentService = studentService;
		this.courseService = courseService;
		this.groupService = groupService;
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

	@GetMapping("/new")
	public String create(Student student, Model model) {
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("genders", Gender.values());
		return "student/create";
	}

	@PostMapping("/save")
	public String save(Student student) {
		retrieveRelationsFields(student);
		studentService.create(student);
		return "redirect:/students";
	}

	private void retrieveRelationsFields(Student student) {
		student.setGroup(groupService.findById(student.getGroup().getId())
				.orElseThrow(() -> new NotFoundEntityException(
						format("Cannot find group by id: %d", student.getGroup().getId()))));
		student.setCourses(student.getCourses()
				.stream()
				.map(c -> courseService.findById(c.getId())
						.orElseThrow(() -> new NotFoundEntityException(
								format("Cannot find course by id: %d", c.getId()))))
				.collect(toSet()));
	}
}
