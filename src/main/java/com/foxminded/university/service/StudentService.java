package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

public interface StudentService extends GenericService<Student> {

	public void setGroupById(Long studentId, Long groupId);

	public void removeGroupById(Long studentId);

	public void addCourseById(Long studentId, Long courseId);

	public void removeCourseById(Long studentId, Long courseId);

	public List<Student> getStudentsByGroup(Group group);

	public List<Student> getStudentsByCourseId(Long courseId);
}
