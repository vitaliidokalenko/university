package com.foxminded.university.api.controller;

import static java.lang.String.format;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.TimeframeService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@RestController
@RequestMapping("/api/v1")
public class TimeframeRestController {

	private final TimeframeService timeframeService;

	public TimeframeRestController(TimeframeService timeframeService) {
		this.timeframeService = timeframeService;
	}

	@GetMapping("/timeframes")
	public Page<Timeframe> getAll(Pageable pageable) {
		return timeframeService.getAllPage(pageable);
	}

	@GetMapping("/timeframes/{id}")
	public ResponseEntity<Timeframe> getById(@PathVariable Long id) {
		Timeframe timeframe = timeframeService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find timeframe by id: %d", id)));
		return ResponseEntity.ok(timeframe);
	}

	@PostMapping("/timeframes")
	public ResponseEntity<Object> create(@Valid @RequestBody Timeframe timeframe) {
		timeframeService.create(timeframe);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/timeframes/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody Timeframe timeframe) {
		timeframeService.update(timeframe);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/timeframes/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		timeframeService.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
