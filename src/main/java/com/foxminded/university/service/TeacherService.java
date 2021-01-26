package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Teacher;

@Service
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
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
		if (isPresentById(id)) {
			teacherDao.deleteById(id);
		}
	}

	private boolean isTeacherValid(Teacher teacher) {
		return teacher.getGender() != null
				&& teacher.getName() != null
				&& teacher.getSurname() != null
				&& !teacher.getName().isEmpty()
				&& !teacher.getSurname().isEmpty();
	}

	private boolean isPresentById(Long id) {
		try {
			return Optional.of(teacherDao.findById(id)).isPresent();
		} catch (EmptyResultDataAccessException exeption) {
			return false;
		}
	}
}
