package com.foxminded.university.controller;

import static java.lang.String.format;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public String getAll(Pageable pageable, Model model) {
		Page<Group> groupsPage = groupService.getAllPage(pageable);
		model.addAttribute("groupsPage", groupsPage);
		return "group/groups";
	}

	@GetMapping("/{id}")
	public String findById(@PathVariable Long id, Model model) {
		Group group = groupService.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find group by id: %d", id)));
		model.addAttribute("group", group);
		return "group/group";
	}

	@GetMapping("/new")
	public String create(Group group, Model model) {
		return "group/create";
	}

	@PostMapping("/save")
	public String save(Group group) {
		groupService.create(group);
		return "redirect:/groups";
	}
}
