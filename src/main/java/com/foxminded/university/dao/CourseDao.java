package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Course;

public interface CourseDao extends GenericDao<Course> {

	public List<Course> getCoursesByRoomId(Long roomId);

	public List<Course> getCoursesByStudentId(Long studentId);

	public List<Course> getCoursesByTeacherId(Long teacherId);
	
	public Optional<Course> findByName(String name);
}
