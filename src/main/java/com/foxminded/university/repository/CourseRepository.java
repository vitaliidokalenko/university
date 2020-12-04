package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.foxminded.university.model.Course;

public class CourseRepository {

	List<Course> courses = new ArrayList<>();
	AtomicInteger id = new AtomicInteger(1);

	public void create(Course course) {
		course.setId(id.getAndIncrement());
		courses.add(course);
	}

	public void deleteById(int id) {
		courses.removeIf(c -> c.getId() == id);
	}

	public List<Course> getCourses() {
		return courses;
	}
}
