package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Course;

public class CourseRepository {

	List<Course> courses = new ArrayList<>();

	public void create(Course course) {
		courses.add(course);
	}

	public void delete(Course course) {
		courses.remove(course);
	}

	public List<Course> getCourses() {
		return courses;
	}
}
