package com.foxminded.university.controller;

import static java.lang.String.format;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Controller
@RequestMapping("/groups")
public class GroupController {

	private final GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}
	
	@GetMapping
	public String getAll(Model model) {
		model.addAttribute("groups", groupService.getAll());
		return "group/groups";
	}
	
	@GetMapping("/{id}")
	public String findById(@PathVariable("id") Long id, Model model) {
		Optional<Group> group = groupService.findById(id);
		if (!group.isPresent()) {
			throw new NotFoundEntityException(format("Cannot find group by id: %d", id));
		}
		model.addAttribute("group", group.get());
		return "group/group";
	}
}
