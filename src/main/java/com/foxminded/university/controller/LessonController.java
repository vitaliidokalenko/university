package com.foxminded.university.controller;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.List;

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

import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CourseService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LessonService;
import com.foxminded.university.service.RoomService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.TimeframeService;

@Controller
@RequestMapping("/lessons")
public class LessonController {

	private final LessonService lessonService;
	private final GroupService groupService;
	private final TeacherService teacherService;
	private final CourseService courseService;
	private final RoomService roomService;
	private final TimeframeService timeframeService;

	public LessonController(LessonService lessonService, GroupService groupService, TeacherService teacherService,
			CourseService courseService, RoomService roomService, TimeframeService timeframeService) {
		this.lessonService = lessonService;
		this.groupService = groupService;
		this.teacherService = teacherService;
		this.courseService = courseService;
		this.roomService = roomService;
		this.timeframeService = timeframeService;
	}

	@GetMapping
	public String getAll(Pageable pageable, Model model) {
		Page<Lesson> lessonsPage = lessonService.getAllPage(pageable);
		model.addAttribute("lessonsPage", lessonsPage);
		return "lesson/lessons";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Lesson lesson = lessonService.findById(id);
		model.addAttribute("lesson", lesson);
		return "lesson/lesson";
	}

	@GetMapping("/new")
	public String create(Model model, Lesson lesson) {
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("teachers", teacherService.getAll());
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("rooms", roomService.getAll());
		model.addAttribute("timeframes", timeframeService.getAll());
		return "lesson/create";
	}

	@GetMapping("/{id}/edit")
	public String update(@PathVariable Long id, Model model) {
		Lesson lesson = lessonService.findById(id);
		model.addAttribute("lesson", lesson);
		model.addAttribute("groups", groupService.getAll());
		model.addAttribute("teachers", teacherService.getAll());
		model.addAttribute("courses", courseService.getAll());
		model.addAttribute("rooms", roomService.getAll());
		model.addAttribute("timeframes", timeframeService.getAll());
		return "lesson/edit";
	}

	@PostMapping("/save")
	public String save(@Valid Lesson lesson, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("groups", groupService.getAll());
			model.addAttribute("teachers", teacherService.getAll());
			model.addAttribute("courses", courseService.getAll());
			model.addAttribute("rooms", roomService.getAll());
			model.addAttribute("timeframes", timeframeService.getAll());
			return "lesson/edit";
		}
		retrieveRelationsFields(lesson);
		if (lesson.getId() == null) {
			lessonService.create(lesson);
		} else {
			lessonService.update(lesson);
		}
		return "redirect:/lessons";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		lessonService.deleteById(id);
		return "redirect:/lessons";
	}

	@PostMapping("/replace/teacher")
	public String replaceTeacher(@RequestParam Long teacherId,
			@RequestParam(value = "substituteTeacherId", required = false) List<Long> substituteTeacherIds,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		Teacher teacher = teacherService.findById(teacherId);
		lessonService.replaceTeacherByDateBetween(teacher, startDate, endDate, substituteTeacherIds);
		return "redirect:/lessons";
	}

	private void retrieveRelationsFields(Lesson lesson) {
		lesson.setCourse(courseService.findById(lesson.getCourse().getId()));
		lesson.setRoom(roomService.findById(lesson.getRoom().getId()));
		lesson.setTeacher(teacherService.findById(lesson.getTeacher().getId()));
		lesson.setTimeframe(timeframeService.findById(lesson.getTimeframe().getId()));
		lesson.setGroups(lesson.getGroups()
				.stream()
				.map(g -> groupService.findById(g.getId()))
				.collect(toSet()));
	}
}
