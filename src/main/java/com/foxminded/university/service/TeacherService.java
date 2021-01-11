package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Teacher;

public interface TeacherService extends GenericService<Teacher> {

	public void addCourseById(Long teacherId, Long courseId);

	public void removeCourseById(Long teacherId, Long courseId);
	
	public List<Teacher> getTeachersByCourseId(Long courseId);
}
