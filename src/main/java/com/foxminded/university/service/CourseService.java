package com.foxminded.university.service;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;

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
		courseDao.create(course);
	}

	@Transactional
	public Course findById(Long id) {
		Course course = courseDao.findById(id);
		course.setRooms(roomDao.getRoomsByCourseId(id).stream().collect(toSet()));
		return course;
	}

	@Transactional
	public List<Course> getAll() {
		return courseDao.getAll();
	}

	@Transactional
	public void update(Course course) {
		courseDao.update(course);
	}

	@Transactional
	public void deleteById(Long id) {
		courseDao.deleteById(id);
	}

	@Transactional
	public void addRoomById(Long courseId, Long roomId) {
		Course course = courseDao.findById(courseId);
		Set<Room> rooms = roomDao.getRoomsByCourseId(courseId).stream().collect(toSet());
		rooms.add(roomDao.findById(roomId));
		course.setRooms(rooms);
		courseDao.update(course);
	}

	@Transactional
	public void removeRoomById(Long courseId, Long roomId) {
		Course course = courseDao.findById(courseId);
		Set<Room> rooms = roomDao.getRoomsByCourseId(courseId).stream().collect(toSet());
		rooms.remove(roomDao.findById(roomId));
		course.setRooms(rooms);
		courseDao.update(course);
	}

	@Transactional
	public List<Course> getCoursesByRoomId(Long roomId) {
		return courseDao.getCoursesByRoomId(roomId);
	}

	@Transactional
	public List<Course> getCoursesByStudentId(Long studentId) {
		return courseDao.getCoursesByStudentId(studentId);
	}

	@Transactional
	public List<Course> getCoursesByTeacherId(Long teacherId) {
		return courseDao.getCoursesByTeacherId(teacherId);
	}
}
