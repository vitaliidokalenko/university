package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

public interface StudentDao extends GenericDao<Student> {

	public List<Student> getStudentsByGroup(Group group);

	public List<Student> getStudentsByCourseId(Long courseId);
}
