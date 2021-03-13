package com.foxminded.university.controller;

import static java.lang.String.format;

import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public String getAll(Pageable pageable, Model model) {
		Page<Course> coursesPage = courseService.getAllPage(pageable);
		model.addAttribute("coursesPage", coursesPage);
		model.addAttribute("numbers", IntStream.rangeClosed(1, coursesPage.getTotalPages()).toArray());
		return "course/courses";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find course by id: %d", id)));
		model.addAttribute("course", course);
		return "course/course";
	}
}
