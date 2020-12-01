package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.foxminded.university.model.Teacher;

public class TeacherRepository {

	List<Teacher> teachers = new ArrayList<>();
	AtomicInteger id = new AtomicInteger(1);

	public void create(Teacher teacher) {
		teacher.setId(id.getAndIncrement());
		teachers.add(teacher);
	}

	public void deleteById(int id) {
		teachers.removeIf(t -> t.getId() == id);
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}
}
