package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Student;

public interface StudentDao {

	public void create(Student student);

	public Student findById(Long studentId);

	public List<Student> getAll();

	public void update(Student student);

	public void deleteById(Long studentId);

	public List<Student> getStudentsByGroupId(Long groupId);

	public void createStudentCourse(Long studentId, Long courseId);

	public void deleteStudentCourse(Long studentId, Long courseId);

	public List<Student> getStudentsByCourseId(Long courseId);
}
