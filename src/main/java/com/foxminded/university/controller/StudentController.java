package com.foxminded.university.controller;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

	private final StudentService studentService;
	private final CourseService courseService;
	private final GroupService groupService;
	private final LessonService lessonService;

	public StudentController(StudentService studentService, CourseService courseService, GroupService groupService,
			LessonService lessonService) {
		this.studentService = studentService;
		this.courseService = courseService;
		this.groupService = groupService;
		this.lessonService = lessonService;
	}

	@GetMapping
	public String getAll(Pageable pageable, Model model) {
		Page<Student> studentsPage = studentService.getAllPage(pageable);
		model.addAttribute("studentsPage", studentsPage);
		return "student/students";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Student student = studentService.findById(id);
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

	@GetMapping("/{id}/edit")
	public String update(@PathVariable Long id, Model model) {
		Student student = studentService.findById(id);
		model.addAttribute("student", student);
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("genders", Gender.values());
		return "student/edit";
	}

	@PostMapping("/save")
	public String save(@Valid Student student, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("courses", courseService.getAll());
			model.addAttribute("groups", groupService.getAll());
			model.addAttribute("genders", Gender.values());
			return "student/edit";
		}
		retrieveRelationsFields(student);
		if (student.getId() == null) {
			studentService.create(student);
		} else {
			studentService.update(student);
		}
		return "redirect:/students";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		studentService.deleteById(id);
		return "redirect:/students";
	}

	@GetMapping("/{id}/timetable")
	public String getTimetable(Model model, @PathVariable Long id,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		Student student = studentService.findById(id);
		model.addAttribute("student", student);
		model.addAttribute("lessons",
				lessonService.getByGroupAndDateBetween(student.getGroup(), startDate, endDate));
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "student/timetable";
	}

	private void retrieveRelationsFields(Student student) {
		student.setGroup(groupService.findById(student.getGroup().getId()));
		student.setCourses(student.getCourses()
				.stream()
				.map(c -> courseService.findById(c.getId()))
				.collect(toSet()));
	}
}
