package com.foxminded.university.controller;

import static java.lang.String.format;

import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public String getAll(Pageable pageable, Model model) {
		Page<Room> roomsPage = roomService.getAllPage(pageable);
		model.addAttribute("roomsPage", roomsPage);
		model.addAttribute("numbers", IntStream.rangeClosed(1, roomsPage.getTotalPages()).toArray());
		return "room/rooms";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Room room = roomService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find room by id: %d", id)));
		model.addAttribute("room", room);
		return "room/room";
	}
}
