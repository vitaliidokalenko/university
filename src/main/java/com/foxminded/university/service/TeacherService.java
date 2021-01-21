package com.foxminded.university.service;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

@Service
public class TeacherService {

	private TeacherDao teacherDao;
	private CourseDao courseDao;

	public TeacherService(TeacherDao teacherDao, CourseDao courseDao) {
		this.teacherDao = teacherDao;
		this.courseDao = courseDao;
	}

	@Transactional
	public void create(Teacher teacher) {
		teacherDao.create(teacher);
	}

	@Transactional
	public Teacher findById(Long id) {
		Teacher teacher = teacherDao.findById(id);
		teacher.setCourses(courseDao.getCoursesByTeacherId(id).stream().collect(toSet()));
		return teacher;
	}

	@Transactional
	public List<Teacher> getAll() {
		return teacherDao.getAll();
	}

	@Transactional
	public void update(Teacher teacher) {
		teacherDao.update(teacher);
	}

	@Transactional
	public void deleteById(Long id) {
		teacherDao.deleteById(id);
	}

	@Transactional
	public void addCourseById(Long teacherId, Long courseId) {
		Teacher teacher = teacherDao.findById(teacherId);
		Set<Course> courses = courseDao.getCoursesByTeacherId(teacherId).stream().collect(toSet());
		courses.add(courseDao.findById(courseId));
		teacher.setCourses(courses);
		teacherDao.update(teacher);
	}

	@Transactional
	public void removeCourseById(Long teacherId, Long courseId) {
		Teacher teacher = teacherDao.findById(teacherId);
		Set<Course> courses = courseDao.getCoursesByTeacherId(teacherId).stream().collect(toSet());
		courses.remove(courseDao.findById(courseId));
		teacher.setCourses(courses);
		teacherDao.update(teacher);
	}

	@Transactional
	public List<Teacher> getTeachersByCourseId(Long courseId) {
		return teacherDao.getTeachersByCourseId(courseId);
	}
}
