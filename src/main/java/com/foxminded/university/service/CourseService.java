package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.service.exception.AlreadyExistsEntityException;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.IncompleteEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Service
public class CourseService {

	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Transactional
	public void create(Course course) {
		verify(course);
		courseDao.create(course);
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
		verify(course);
		courseDao.update(course);
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			courseDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(format("There is nothing to delete. Course with id: %d is absent", id));
		}
	}

	private void verify(Course course) {
		if (course.getName() == null) {
			throw new IllegalFieldEntityException("The name of the course is absent");
		} else if (course.getName().isEmpty()) {
			throw new IllegalFieldEntityException("The name of the course is empty");
		} else if (!isNameUnique(course)) {
			throw new AlreadyExistsEntityException(format("The course with name %s already exists", course.getName()));
		} else if (course.getRooms().isEmpty()) {
			throw new IncompleteEntityException(
					format("There are no rooms assigned to the course: %s", course.getName()));
		}
	}

	private boolean isPresentById(Long id) {
		return courseDao.findById(id).isPresent();
	}

	private boolean isNameUnique(Course course) {
		Optional<Course> courseByName = courseDao.findByName(course.getName());
		if (courseByName.isPresent()) {
			return courseByName.get().getId().equals(course.getId());
		} else {
			return true;
		}
	}
}
