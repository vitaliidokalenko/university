package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.IncompleteEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Service
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	@Transactional
	public void create(Teacher teacher) {
		verify(teacher);
		teacherDao.create(teacher);
	}

	@Transactional
	public Optional<Teacher> findById(Long id) {
		return teacherDao.findById(id);
	}

	@Transactional
	public List<Teacher> getAll() {
		return teacherDao.getAll();
	}

	@Transactional
	public void update(Teacher teacher) {
		verify(teacher);
		teacherDao.update(teacher);
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			teacherDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(format("There is nothing to delete. Teacher with id: %d is absent", id));
		}
	}

	private void verify(Teacher teacher) {
		if (teacher.getName() == null) {
			throw new IllegalFieldEntityException("The name of the teacher is absent");
		} else if (teacher.getSurname() == null) {
			throw new IllegalFieldEntityException("The surname of the teacher is absent");
		} else if (teacher.getName().isEmpty()) {
			throw new IllegalFieldEntityException("The name of the teacher is empty");
		} else if (teacher.getSurname().isEmpty()) {
			throw new IllegalFieldEntityException("The surname of the teacher is empty");
		} else if (teacher.getGender() == null) {
			throw new IllegalFieldEntityException(
					format("Gender of the teacher %s %s is absent", teacher.getName(), teacher.getSurname()));
		} else if (teacher.getCourses().isEmpty()) {
			throw new IncompleteEntityException(format("There are no courses assigned to the teacher %s %s",
					teacher.getName(),
					teacher.getSurname()));
		}
	}

	private boolean isPresentById(Long id) {
		return teacherDao.findById(id).isPresent();
	}
}
