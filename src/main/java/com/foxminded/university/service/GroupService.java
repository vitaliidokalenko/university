package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Group;

public interface GroupService extends GenericService<Group> {

	public List<Group> getGroupsByLessonId(Long lessonId);
}
