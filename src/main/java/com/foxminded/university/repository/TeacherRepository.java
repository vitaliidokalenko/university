package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Teacher;

public class TeacherRepository {

	List<Teacher> teachers = new ArrayList<>();

	public void create(Teacher teacher) {
		teachers.add(teacher);
	}

	public void delete(Teacher teacher) {
		teachers.remove(teacher);
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}
}
