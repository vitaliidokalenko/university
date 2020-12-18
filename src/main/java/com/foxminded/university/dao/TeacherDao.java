package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Teacher;

public interface TeacherDao {

	public void create(Teacher teacher);

	public Teacher findById(Long teacherId);

	public List<Teacher> getAll();

	public void update(Teacher teacher);

	public void deleteById(Long teacherId);
	
	public void createTeacherCourse(Long teacherId, Long courseId);
	
	public void deleteTeacherCourse(Long teacherId, Long courseId);
	
	public List<Teacher> getTeachersByCourseId(Long courseId);
}
