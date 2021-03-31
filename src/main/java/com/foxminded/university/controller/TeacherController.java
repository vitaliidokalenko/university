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
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private final TeacherService teacherService;
	private final CourseService courseService;

	public TeacherController(TeacherService teacherService, CourseService courseService) {
		this.teacherService = teacherService;
		this.courseService = courseService;
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

	@GetMapping("/new")
	public String create(Teacher teacher, Model model) {
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("genders", Gender.values());
		return "teacher/create";
	}

	@GetMapping("/{id}/edit")
	public String update(@PathVariable Long id, Model model) {
		Teacher teacher = teacherService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find teacher by id: %d", id)));
		model.addAttribute("teacher", teacher);
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("genders", Gender.values());
		return "teacher/edit";
	}

	@PostMapping("/save")
	public String save(Teacher teacher) {
		retrieveRelationsFields(teacher);
		if (teacher.getId() == null) {
			teacherService.create(teacher);
		} else {
			teacherService.update(teacher);
		}
		return "redirect:/teachers";
	}

	@GetMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		teacherService.deleteById(id);
		return "redirect:/teachers";
	}

	private void retrieveRelationsFields(Teacher teacher) {
		teacher.setCourses(teacher.getCourses()
				.stream()
				.map(c -> courseService.findById(c.getId())
						.orElseThrow(() -> new NotFoundEntityException(
								format("Cannot find course by id: %d", c.getId()))))
				.collect(toSet()));
	}
}
