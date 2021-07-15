package com.foxminded.university.api.controller;

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

import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupRestController {

	private final GroupService groupService;

	public GroupRestController(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping
	public Page<Group> getAll(Pageable pageable) {
		return groupService.getAllPage(pageable);
	}

	@GetMapping("/{id}")
	public Group getById(@PathVariable Long id) {
		return groupService.findById(id);
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody Group group) {
		groupService.create(group);
		return ResponseEntity.created(linkTo(methodOn(GroupRestController.class).getById(group.getId())).toUri())
				.build();
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @Valid @RequestBody Group group) {
		groupService.update(group);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		groupService.deleteById(id);
	}
}
