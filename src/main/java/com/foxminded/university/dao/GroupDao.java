package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Group;

public interface GroupDao {

	public void create(Group group);

	public Group findById(Long groupId);

	public List<Group> getAll();

	public void update(Group group);

	public void deleteById(Long groupId);

	public List<Group> getGroupsByLessonId(Long lessonId);
}
