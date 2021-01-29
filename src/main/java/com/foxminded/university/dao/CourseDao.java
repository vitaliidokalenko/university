package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Course;

public interface CourseDao extends GenericDao<Course> {

	public List<Course> getByRoomId(Long roomId);

	public List<Course> getByStudentId(Long studentId);

	public List<Course> getByTeacherId(Long teacherId);

	public Optional<Course> findByName(String name);
}
