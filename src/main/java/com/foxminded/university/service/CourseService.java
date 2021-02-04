package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.exception.DAOException;
import com.foxminded.university.model.Course;
import com.foxminded.university.service.exception.ServiceException;

@Service
public class CourseService {

	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Transactional
	public void create(Course course) {
		if (isCourseValid(course)) {
			try {
				courseDao.create(course);
			} catch (DAOException e) {
				throw new ServiceException("Could not create course: " + course, e);
			}
		}
	}

	@Transactional
	public Optional<Course> findById(Long id) {
		try {
			return courseDao.findById(id);
		} catch (DAOException e) {
			throw new ServiceException("Could not get course by id: " + id, e);
		}
	}

	@Transactional
	public List<Course> getAll() {
		try {
			return courseDao.getAll();
		} catch (DAOException e) {
			throw new ServiceException("Could not get courses", e);
		}
	}

	@Transactional
	public void update(Course course) {
		if (isCourseValid(course)) {
			try {
				courseDao.update(course);
			} catch (DAOException e) {
				throw new ServiceException("Could not update course: " + course, e);
			}
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			try {
				courseDao.deleteById(id);
			} catch (DAOException e) {
				throw new ServiceException("Could not delete course by id: " + id, e);
			}
		}
	}

	private boolean isCourseValid(Course course) {
		return course.getName() != null
				&& !course.getName().isEmpty()
				&& isNameUnique(course)
				&& !course.getRooms().isEmpty();
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
