package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourseService {

	private CourseDao courseDao;

	public CourseService(CourseDao courseDao) {
		this.courseDao = courseDao;
	}

	@Transactional
	public void create(Course course) {
		log.debug("Creating course: {}", course);
		verify(course);
		courseDao.save(course);
	}

	@Transactional
	public Course findById(Long id) {
		log.debug("Finding course by id: {}", id);
		return courseDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find course by id: %d", id)));
	}

	@Transactional
	public List<Course> getAll() {
		log.debug("Getting courses");
		return courseDao.findAll();
	}

	@Transactional
	public Page<Course> getAllPage(Pageable pageable) {
		log.debug("Getting pageable courses");
		return courseDao.findAll(pageable);
	}

	@Transactional
	public void update(Course course) {
		if (findById(course.getId()) != null) {
			verify(course);
			courseDao.save(course);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		log.debug("Deleting course by id: {}", id);
		courseDao.delete(findById(id));
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
