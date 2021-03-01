package com.foxminded.university.controller;

import static java.lang.String.format;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Course;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Controller
@RequestMapping("/courses")
public class CourseController {

	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("courses", courseService.getAll());
		return "course/courses";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable("id") Long id, Model model) {
		Optional<Course> course = courseService.findById(id);
		if (!course.isPresent()) {
			throw new NotFoundEntityException(format("Cannot find course by id: %d", id));
		}
		model.addAttribute("course", course.get());
		return "course/course";
	}
}
