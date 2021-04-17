package com.foxminded.university.controller.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.foxminded.university.model.Course;

public class CourseFormatter implements Formatter<Course> {

	@Override
	public String print(Course course, Locale locale) {
		return Long.toString(course.getId());
	}

	@Override
	public Course parse(String id, Locale locale) throws ParseException {
		Course course = new Course();
		course.setId(Long.valueOf(id));
		return course;
	}

}
