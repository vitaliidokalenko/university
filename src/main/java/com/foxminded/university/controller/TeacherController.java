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
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private final TeacherService teacherService;
	private final CourseService courseService;
	private final LessonService lessonService;

	public TeacherController(TeacherService teacherService, CourseService courseService, LessonService lessonService) {
		this.teacherService = teacherService;
		this.courseService = courseService;
		this.lessonService = lessonService;
	}

	@GetMapping
	public String getAll(Pageable pageable, Model model) {
		Page<Teacher> teachersPage = teacherService.getAllPage(pageable);
		model.addAttribute("teachersPage", teachersPage);
		return "teacher/teachers";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Teacher teacher = teacherService.findById(id);
		model.addAttribute("teacher", teacher);
		return "teacher/teacher";
	}

	@GetMapping("/new")
	public String create(Teacher teacher, Model model) {
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("genders", Gender.values());
		return "teacher/create";
	}

	@GetMapping("/{id}/edit")
	public String update(@PathVariable Long id, Model model) {
		Teacher teacher = teacherService.findById(id);
		model.addAttribute("teacher", teacher);
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("genders", Gender.values());
		return "teacher/edit";
	}

	@PostMapping("/save")
	public String save(@Valid Teacher teacher, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("courses", courseService.getAll());
			model.addAttribute("genders", Gender.values());
			return "teacher/edit";
		}
		retrieveRelationsFields(teacher);
		if (teacher.getId() == null) {
			teacherService.create(teacher);
		} else {
			teacherService.update(teacher);
		}
		return "redirect:/teachers";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		teacherService.deleteById(id);
		return "redirect:/teachers";
	}

	@GetMapping("/{id}/timetable")
	public String getTimetable(Model model, @PathVariable Long id,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		Teacher teacher = teacherService.findById(id);
		model.addAttribute("teacher", teacher);
		model.addAttribute("lessons",
				lessonService.getByTeacherAndDateBetween(teacher, startDate, endDate));
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "teacher/timetable";
	}

	@GetMapping("/{id}/replace")
	public String replace(Model model, @PathVariable Long id) {
		Teacher teacher = teacherService.findById(id);
		model.addAttribute("teacher", teacher);
		model.addAttribute("substituteTeachers", teacherService.getSubstituteTeachers(teacher));
		return "teacher/replace";
	}

	private void retrieveRelationsFields(Teacher teacher) {
		teacher.setCourses(teacher.getCourses()
				.stream()
				.map(c -> courseService.findById(c.getId()))
				.collect(toSet()));
	}
}
