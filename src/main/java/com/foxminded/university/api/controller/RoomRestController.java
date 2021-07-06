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

import com.foxminded.university.model.Room;
import com.foxminded.university.service.RoomService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomRestController {

	private final RoomService roomService;

	public RoomRestController(RoomService roomService) {
		this.roomService = roomService;
	}

	@GetMapping
	public Page<Room> getAll(Pageable pageable) {
		return roomService.getAllPage(pageable);
	}

	@GetMapping("/{id}")
	public Room getById(@PathVariable Long id) {
		return roomService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find room by id: %d", id)));
	}

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody Room room) {
		roomService.create(room);
		return ResponseEntity.created(linkTo(methodOn(RoomRestController.class).getById(room.getId())).toUri())
				.build();
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @Valid @RequestBody Room room) {
		roomService.update(room);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		roomService.deleteById(id);
	}
}
