package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Group;

public interface GroupService extends GenericService<Group> {

	public void addStudentById(Long groupId, Long studentId);

	public void removeStudentById(Long groupId, Long studentId);
	
	public List<Group> getGroupsByLessonId(Long lessonId);
}
