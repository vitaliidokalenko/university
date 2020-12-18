package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Course;

public interface CourseDao {

	public void create(Course course);

	public Course findById(Long courseId);

	public List<Course> getAll();

	public void update(Course course);

	public void deleteById(Long courseId);

	public void createCourseRoom(Long courseId, Long roomId);

	public void deleteCourseRoom(Long courseId, Long roomId);

	public List<Course> getCoursesByRoomId(Long roomId);

	public List<Course> getCoursesByStudentId(Long studentId);
	
	public List<Course> getCoursesByTeacherId(Long teacherId);
}
