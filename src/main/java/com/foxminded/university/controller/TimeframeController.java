package com.foxminded.university.controller;

import static java.lang.String.format;

import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		model.addAttribute("numbers", IntStream.rangeClosed(1, timeframesPage.getTotalPages()).toArray());
		return "timeframe/timeframes";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable("id") Long id, Model model) {
		Optional<Timeframe> timeframe = timeframeService.findById(id);
		if (!timeframe.isPresent()) {
			throw new NotFoundEntityException(format("Cannot find timeframe by id: %d", id));
		}
		model.addAttribute("timeframe", timeframe.get());
		return "timeframe/timeframe";
	}
}
