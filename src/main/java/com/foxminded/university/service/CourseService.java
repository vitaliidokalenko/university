package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;

@Service
public class CourseService {

	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Transactional
	public void create(Course course) {
		if (isCourseValid(course)) {
			courseDao.create(course);
		}
	}

	@Transactional
	public Optional<Course> findById(Long id) {
		return courseDao.findById(id);
	}

	@Transactional
	public List<Course> getAll() {
		return courseDao.getAll();
	}

	@Transactional
	public void update(Course course) {
		if (isCourseValid(course)) {
			courseDao.update(course);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			courseDao.deleteById(id);
		}
	}

	private boolean isCourseValid(Course course) {
		return course.getName() != null
				&& !course.getName().isEmpty()
				&& !course.getRooms().isEmpty();
	}

	private boolean isPresentById(Long id) {
		return courseDao.findById(id).isPresent();
	}
}
