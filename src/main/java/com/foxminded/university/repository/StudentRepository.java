package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.foxminded.university.model.Student;

public class StudentRepository {

	List<Student> students = new ArrayList<>();
	AtomicInteger id = new AtomicInteger(1);

	public void create(Student student) {
		student.setId(id.getAndIncrement());
		students.add(student);
	}

	public void deleteById(int id) {
		students.removeIf(s -> s.getId() == id);
	}

	public List<Student> getStudents() {
		return students;
	}
}
