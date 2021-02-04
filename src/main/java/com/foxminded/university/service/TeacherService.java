package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.exception.DAOException;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.exception.ServiceException;

@Service
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	@Transactional
	public void create(Teacher teacher) {
		if (isTeacherValid(teacher)) {
			try {
				teacherDao.create(teacher);
			} catch (DAOException e) {
				throw new ServiceException("Could not create teacher: " + teacher, e);
			}
		}
	}

	@Transactional
	public Optional<Teacher> findById(Long id) {
		try {
			return teacherDao.findById(id);
		} catch (DAOException e) {
			throw new ServiceException("Could not get teacher by id: " + id, e);
		}
	}

	@Transactional
	public List<Teacher> getAll() {
		try {
			return teacherDao.getAll();
		} catch (DAOException e) {
			throw new ServiceException("Could not get teachers", e);
		}
	}

	@Transactional
	public void update(Teacher teacher) {
		if (isTeacherValid(teacher)) {
			try {
				teacherDao.update(teacher);
			} catch (DAOException e) {
				throw new ServiceException("Could not update teacher: " + teacher, e);
			}
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			try {
				teacherDao.deleteById(id);
			} catch (DAOException e) {
				throw new ServiceException("Could not delete teacher by id: " + id, e);
			}
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
		return teacherDao.findById(id).isPresent();
	}
}
