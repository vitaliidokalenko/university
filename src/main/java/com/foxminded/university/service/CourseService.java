package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

@Service
public class CourseService {

	private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Transactional
	public void create(Course course) {
		logger.debug("Creating course: {}", course);
		verify(course);
		courseDao.save(course);
	}

	@Transactional
	public Optional<Course> findById(Long id) {
		logger.debug("Finding course by id: {}", id);
		return courseDao.findById(id);
	}

	@Transactional
	public List<Course> getAll() {
		logger.debug("Getting courses");
		return courseDao.findAll();
	}

	@Transactional
	public Page<Course> getAllPage(Pageable pageable) {
		logger.debug("Getting pageable courses");
		return courseDao.findAll(pageable);
	}

	@Transactional
	public void update(Course course) {
		logger.debug("Updating course: {}", course);
		verify(course);
		courseDao.save(course);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting course by id: {}", id);
		courseDao.delete(courseDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find course by id: %d", id))));
	}

	private void verify(Course course) {
		verifyNameIsUnique(course);
	}

	private void verifyNameIsUnique(Course course) {
		Optional<Course> courseByName = courseDao.findByName(course.getName());
		if (courseByName.isPresent() && !courseByName.get().getId().equals(course.getId())) {
			throw new NotUniqueNameException(format("The course with name %s already exists", course.getName()));
		}
	}
}
