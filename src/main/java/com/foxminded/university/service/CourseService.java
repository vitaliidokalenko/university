package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Course;

public interface CourseService extends GenericService<Course> {

	public void addRoomById(Long courseId, Long roomId);

	public void removeRoomById(Long courseId, Long roomId);

	public List<Course> getCoursesByRoomId(Long roomId);

	public List<Course> getCoursesByStudentId(Long studentId);

	public List<Course> getCoursesByTeacherId(Long teacherId);
}
