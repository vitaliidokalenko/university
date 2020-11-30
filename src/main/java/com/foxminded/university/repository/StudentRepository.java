package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Student;

public class StudentRepository {

	List<Student> students = new ArrayList<>();

	public void create(Student student) {
		students.add(student);
	}

	public void delete(Student student) {
		students.remove(student);
	}

	public List<Student> getStudents() {
		return students;
	}
}
