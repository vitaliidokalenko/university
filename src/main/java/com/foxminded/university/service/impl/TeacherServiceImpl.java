package com.foxminded.university.service.impl;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService {

	private TeacherDao teacherDao;
	private CourseDao courseDao;

	public TeacherServiceImpl(TeacherDao teacherDao, CourseDao courseDao) {
		this.teacherDao = teacherDao;
		this.courseDao = courseDao;
	}

	@Override
	@Transactional
	public void create(Teacher teacher) {
		teacherDao.create(teacher);
	}

	@Override
	@Transactional
	public Teacher findById(Long id) {
		Teacher teacher = teacherDao.findById(id);
		teacher.setCourses(courseDao.getCoursesByTeacherId(id).stream().collect(toSet()));
		return teacher;
	}

	@Override
	@Transactional
	public List<Teacher> getAll() {
		return teacherDao.getAll();
	}

	@Override
	@Transactional
	public void update(Teacher teacher) {
		teacherDao.update(teacher);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		teacherDao.deleteById(id);
	}

	@Override
	@Transactional
	public void addCourseById(Long teacherId, Long courseId) {
		Teacher teacher = teacherDao.findById(teacherId);
		Set<Course> courses = courseDao.getCoursesByTeacherId(teacherId).stream().collect(toSet());
		courses.add(courseDao.findById(courseId));
		teacher.setCourses(courses);
		teacherDao.update(teacher);
	}

	@Override
	@Transactional
	public void removeCourseById(Long teacherId, Long courseId) {
		Teacher teacher = teacherDao.findById(teacherId);
		Set<Course> courses = courseDao.getCoursesByTeacherId(teacherId).stream().collect(toSet());
		courses.remove(courseDao.findById(courseId));
		teacher.setCourses(courses);
		teacherDao.update(teacher);
	}

	@Override
	@Transactional
	public List<Teacher> getTeachersByCourseId(Long courseId) {
		return teacherDao.getTeachersByCourseId(courseId);
	}

	@Override
	@Transactional
	public boolean existsById(Long id) {
		return Optional.of(teacherDao.findById(id)).isPresent();
	}
}
