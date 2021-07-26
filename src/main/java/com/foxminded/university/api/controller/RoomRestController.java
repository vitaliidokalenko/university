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

import com.foxminded.university.api.controller.swagger.annotation.ApiPageable;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.RoomService;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomRestController {

	private final RoomService roomService;

	public RoomRestController(RoomService roomService) {
		this.roomService = roomService;
	}

	@ApiPageable
	@GetMapping
	public Page<Room> getAll(Pageable pageable) {
		return roomService.getAllPage(pageable);
	}

	@ApiResponses(value = { @ApiResponse(code = 404, message = "Not Found") })
	@GetMapping("/{id}")
	public Room getById(@PathVariable Long id) {
		return roomService.findById(id);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 409, message = "Conflict")
	})
	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody Room room) {
		roomService.create(room);
		return ResponseEntity.created(linkTo(methodOn(RoomRestController.class).getById(room.getId())).toUri())
				.build();
	}

	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 409, message = "Conflict")
	})
	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @Valid @RequestBody Room room) {
		roomService.update(room);
	}

	@ApiResponses(value = { @ApiResponse(code = 404, message = "Not Found") })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		roomService.deleteById(id);
	}
}
