package com.foxminded.university.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.TimeframeService;

@Controller
@RequestMapping("/timeframes")
public class TimeframeController {

	private final TimeframeService timeframeService;

	public TimeframeController(TimeframeService timeframeService) {
		this.timeframeService = timeframeService;
	}

	@GetMapping
	public String getAll(Pageable pageable, Model model) {
		Page<Timeframe> timeframesPage = timeframeService.getAllPage(pageable);
		model.addAttribute("timeframesPage", timeframesPage);
		return "timeframe/timeframes";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Timeframe timeframe = timeframeService.findById(id);
		model.addAttribute("timeframe", timeframe);
		return "timeframe/timeframe";
	}

	@GetMapping("/new")
	public String create(Timeframe timeframe, Model model) {
		return "timeframe/create";
	}

	@GetMapping("/{id}/edit")
	public String update(@PathVariable Long id, Model model) {
		Timeframe timeframe = timeframeService.findById(id);
		model.addAttribute("timeframe", timeframe);
		return "timeframe/edit";
	}

	@PostMapping("/save")
	public String save(@Valid Timeframe timeframe, BindingResult result) {
		if (result.hasErrors()) {
			return "timeframe/edit";
		}
		if (timeframe.getId() == null) {
			timeframeService.create(timeframe);
		} else {
			timeframeService.update(timeframe);
		}
		return "redirect:/timeframes";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		timeframeService.deleteById(id);
		return "redirect:/timeframes";
	}
}
