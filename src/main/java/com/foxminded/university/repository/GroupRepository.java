package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Group;

public class GroupRepository {

	List<Group> groups = new ArrayList<>();

	public void create(Group group) {
		groups.add(group);
	}

	public void delete(Group group) {
		groups.remove(group);
	}

	public List<Group> getGroups() {
		return groups;
	}
}
