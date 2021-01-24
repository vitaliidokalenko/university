package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Course;

@Service
public class CourseService {

	private CourseDao courseDao;
	private RoomDao roomDao;

	public CourseService(CourseDao courseDao, RoomDao roomDao) {
		this.courseDao = courseDao;
		this.roomDao = roomDao;
	}

	@Transactional
	public void create(Course course) {
		if (isCourseValid(course)) {
			courseDao.create(course);
		}
	}

	@Transactional
	public Course findById(Long id) {
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

	@Transactional
	public void addRoomById(Long courseId, Long roomId) {
		Course course = courseDao.findById(courseId);
		course.getRooms().add(roomDao.findById(roomId));
		courseDao.update(course);
	}

	@Transactional
	public void removeRoomById(Long courseId, Long roomId) {
		Course course = courseDao.findById(courseId);
		course.getRooms().remove(roomDao.findById(roomId));
		courseDao.update(course);
	}

	@Transactional
	public List<Course> getCoursesByRoomId(Long roomId) {
		return courseDao.getCoursesByRoomId(roomId);
	}

	private boolean isCourseValid(Course course) {
		return course.getName() != null
				&& !course.getName().isEmpty()
				&& !course.getRooms().isEmpty();
	}

	private boolean isPresentById(Long id) {
		try {
			return Optional.of(courseDao.findById(id)).isPresent();
		} catch (EmptyResultDataAccessException exeption) {
			return false;
		}
	}
}
