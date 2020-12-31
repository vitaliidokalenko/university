package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

public interface StudentDao {

	public void create(Student student);

	public Student findById(Long studentId);

	public List<Student> getAll();

	public void update(Student student);

	public void deleteById(Long studentId);

	public List<Student> getStudentsByGroup(Group group);

	public List<Student> getStudentsByCourseId(Long courseId);
}
