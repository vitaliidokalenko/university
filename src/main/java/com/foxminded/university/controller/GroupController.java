package com.foxminded.university.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Group;
import com.foxminded.university.service.GroupService;

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
		Group group = groupService.findById(id);
		model.addAttribute("group", group);
		return "group/group";
	}

	@GetMapping("/new")
	public String create(Group group, Model model) {
		return "group/create";
	}

	@GetMapping("/{id}/edit")
	public String update(@PathVariable Long id, Model model) {
		Group group = groupService.findById(id);
		model.addAttribute("group", group);
		return "group/edit";
	}

	@PostMapping("/save")
	public String save(@Valid Group group, BindingResult result) {
		if (result.hasErrors()) {
			return "group/edit";
		}
		if (group.getId() == null) {
			groupService.create(group);
		} else {
			groupService.update(group);
		}
		return "redirect:/groups";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		groupService.deleteById(id);
		return "redirect:/groups";
	}
}
