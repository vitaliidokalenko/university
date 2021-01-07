package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Group;

public interface GroupDao extends GenericDao<Group> {

	public List<Group> getGroupsByLessonId(Long lessonId);
}
