package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.TeacherDao;
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
		if (isTeacherValid(teacher)) {
			teacherDao.create(teacher);
		}
	}

	@Transactional
	public Teacher findById(Long id) {
		return teacherDao.findById(id);
	}

	@Transactional
	public List<Teacher> getAll() {
		return teacherDao.getAll();
	}

	@Transactional
	public void update(Teacher teacher) {
		if (isTeacherValid(teacher)) {
			teacherDao.update(teacher);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		teacherDao.deleteById(id);
	}

	@Transactional
	public void addCourseById(Long teacherId, Long courseId) {
		Teacher teacher = teacherDao.findById(teacherId);
		teacher.getCourses().add(courseDao.findById(courseId));
		teacherDao.update(teacher);
	}

	@Transactional
	public void removeCourseById(Long teacherId, Long courseId) {
		Teacher teacher = teacherDao.findById(teacherId);
		teacher.getCourses().remove(courseDao.findById(courseId));
		teacherDao.update(teacher);
	}

	@Transactional
	public List<Teacher> getTeachersByCourseId(Long courseId) {
		return teacherDao.getTeachersByCourseId(courseId);
	}

	private boolean isTeacherValid(Teacher teacher) {
		return teacher.getGender() != null
				&& teacher.getName() != null
				&& teacher.getSurname() != null
				&& !teacher.getName().isEmpty()
				&& !teacher.getSurname().isEmpty();
	}
}
