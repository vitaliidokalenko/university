package com.foxminded.university.controller;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Course;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.RoomService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Controller
@RequestMapping("/courses")
public class CourseController {

	private final CourseService courseService;
	private final RoomService roomService;

	public CourseController(CourseService courseService, RoomService roomService) {
		this.courseService = courseService;
		this.roomService = roomService;
	}

	@GetMapping
	public String getAll(Pageable pageable, Model model) {
		Page<Course> coursesPage = courseService.getAllPage(pageable);
		model.addAttribute("coursesPage", coursesPage);
		return "course/courses";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find course by id: %d", id)));
		model.addAttribute("course", course);
		return "course/course";
	}

	@GetMapping("/new")
	public String create(Course course, Model model) {
		model.addAttribute("rooms", roomService.getAll());
		return "course/create";
	}

	@GetMapping("/{id}/edit")
	public String update(@PathVariable Long id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find course by id: %d", id)));
		model.addAttribute("course", course);
		model.addAttribute("rooms", roomService.getAll());
		return "course/edit";
	}

	@PostMapping("/save")
	public String save(@Valid Course course) {
		retrieveRelationsFields(course);
		if (course.getId() == null) {
			courseService.create(course);
		} else {
			courseService.update(course);
		}
		return "redirect:/courses";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		courseService.deleteById(id);
		return "redirect:/courses";
	}

	private void retrieveRelationsFields(Course course) {
		course.setRooms(course.getRooms()
				.stream()
				.map(r -> roomService.findById(r.getId())
						.orElseThrow(() -> new NotFoundEntityException(
								format("Cannot find room by id: %d", r.getId()))))
				.collect(toSet()));
	}
}
