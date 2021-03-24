package com.foxminded.university.controller;

import static java.lang.String.format;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.TimeframeService;
import com.foxminded.university.service.exception.NotFoundEntityException;

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
		Timeframe timeframe = timeframeService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find timeframe by id: %d", id)));
		model.addAttribute("timeframe", timeframe);
		return "timeframe/timeframe";
	}

	@GetMapping("/new")
	public String create(Timeframe timeframe, Model model) {
		return "timeframe/create";
	}

	@PostMapping("/save")
	public String save(Timeframe timeframe) {
		timeframeService.create(timeframe);
		return "redirect:/timeframes";
	}
}
