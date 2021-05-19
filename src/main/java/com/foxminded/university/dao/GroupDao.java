package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Group;

public interface GroupDao extends GenericDao<Group> {

	public List<Group> getAll();

	public List<Group> getByLessonId(Long lessonId);

	Optional<Group> findByName(String name);
}
