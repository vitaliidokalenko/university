package com.foxminded.university.controller;

import static java.lang.String.format;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Room;
import com.foxminded.university.service.RoomService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Controller
@RequestMapping("/rooms")
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("rooms", roomService.getAll());
		return "room/rooms";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable("id") Long id, Model model) {
		Optional<Room> room = roomService.findById(id);
		if (!room.isPresent()) {
			throw new NotFoundEntityException(format("Cannot find room by id: %d", id));
		}
		model.addAttribute("room", room.get());
		return "room/room";
	}
}
