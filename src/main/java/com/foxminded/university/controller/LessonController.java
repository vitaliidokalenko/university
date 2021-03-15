package com.foxminded.university.controller;

import static java.lang.String.format;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Lesson;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Controller
@RequestMapping("/lessons")
public class LessonController {

	private final LessonService lessonService;

	public LessonController(LessonService lessonService) {
		this.lessonService = lessonService;
	}

	@GetMapping
	public String getAll(Pageable pageable, Model model) {
		Page<Lesson> lessonsPage = lessonService.getAllPage(pageable);
		model.addAttribute("lessonsPage", lessonsPage);
		return "lesson/lessons";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Lesson lesson = lessonService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find lesson by id: %d", id)));
		model.addAttribute("lesson", lesson);
		return "lesson/lesson";
	}
}
