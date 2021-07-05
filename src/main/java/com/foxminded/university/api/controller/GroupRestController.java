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

import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@RestController
@RequestMapping("/api/v1")
public class GroupRestController {

	private final GroupService groupService;

	public GroupRestController(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping("/groups")
	public Page<Group> getAll(Pageable pageable) {
		return groupService.getAllPage(pageable);
	}

	@GetMapping("/groups/{id}")
	public ResponseEntity<Group> getById(@PathVariable Long id) {
		Group group = groupService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find group by id: %d", id)));
		return ResponseEntity.ok(group);
	}

	@PostMapping("/groups")
	public ResponseEntity<Object> create(@Valid @RequestBody Group group) {
		groupService.create(group);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/groups/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody Group group) {
		groupService.update(group);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/groups/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {
		groupService.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
