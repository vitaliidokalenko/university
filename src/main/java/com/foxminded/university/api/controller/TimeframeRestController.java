package com.foxminded.university.api.controller;

import static java.lang.String.format;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.TimeframeService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@RestController
@RequestMapping("/api/v1/timeframes")
public class TimeframeRestController {

	private final TimeframeService timeframeService;

	public TimeframeRestController(TimeframeService timeframeService) {
		this.timeframeService = timeframeService;
	}

	@GetMapping
	public Page<Timeframe> getAll(Pageable pageable) {
		return timeframeService.getAllPage(pageable);
	}

	@GetMapping("/{id}")
	public Timeframe getById(@PathVariable Long id) {
		return timeframeService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find timeframe by id: %d", id)));
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody Timeframe timeframe) {
		timeframeService.create(timeframe);
		return ResponseEntity
				.created(linkTo(methodOn(TimeframeRestController.class).getById(timeframe.getId())).toUri())
				.build();
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @Valid @RequestBody Timeframe timeframe) {
		timeframeService.update(timeframe);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		timeframeService.deleteById(id);
	}
}
