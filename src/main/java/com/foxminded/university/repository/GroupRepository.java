package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.foxminded.university.model.Group;

public class GroupRepository {

	List<Group> groups = new ArrayList<>();
	AtomicInteger id = new AtomicInteger(1);

	public void create(Group group) {
		group.setId(id.getAndIncrement());
		groups.add(group);
	}

	public void deleteById(int id) {
		groups.removeIf(g -> g.getId() == id);
	}

	public List<Group> getGroups() {
		return groups;
	}
}
