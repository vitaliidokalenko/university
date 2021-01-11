package com.foxminded.university.service.impl;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

	private CourseDao courseDao;
	private RoomDao roomDao;

	public CourseServiceImpl(CourseDao courseDao, RoomDao roomDao) {
		this.courseDao = courseDao;
		this.roomDao = roomDao;
	}

	@Override
	@Transactional
	public void create(Course course) {
		courseDao.create(course);
	}

	@Override
	@Transactional
	public Course findById(Long id) {
		Course course = courseDao.findById(id);
		course.setRooms(roomDao.getRoomsByCourseId(id).stream().collect(toSet()));
		return course;
	}

	@Override
	@Transactional
	public List<Course> getAll() {
		return courseDao.getAll();
	}

	@Override
	@Transactional
	public void update(Course course) {
		courseDao.update(course);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		courseDao.deleteById(id);
	}

	@Override
	@Transactional
	public void addRoomById(Long courseId, Long roomId) {
		Course course = courseDao.findById(courseId);
		Set<Room> rooms = roomDao.getRoomsByCourseId(courseId).stream().collect(toSet());
		rooms.add(roomDao.findById(roomId));
		course.setRooms(rooms);
		courseDao.update(course);
	}

	@Override
	@Transactional
	public void removeRoomById(Long courseId, Long roomId) {
		Course course = courseDao.findById(courseId);
		Set<Room> rooms = roomDao.getRoomsByCourseId(courseId).stream().collect(toSet());
		rooms.remove(roomDao.findById(roomId));
		course.setRooms(rooms);
		courseDao.update(course);
	}

	@Override
	@Transactional
	public List<Course> getCoursesByRoomId(Long roomId) {
		return courseDao.getCoursesByRoomId(roomId);
	}

	@Override
	@Transactional
	public List<Course> getCoursesByStudentId(Long studentId) {
		return courseDao.getCoursesByStudentId(studentId);
	}

	@Override
	@Transactional
	public List<Course> getCoursesByTeacherId(Long teacherId) {
		return courseDao.getCoursesByTeacherId(teacherId);
	}

	@Override
	@Transactional
	public boolean existsById(Long id) {
		return Optional.of(courseDao.findById(id)).isPresent();
	}
}
