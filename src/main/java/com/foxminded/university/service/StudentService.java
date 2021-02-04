package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.exception.DAOException;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.exception.ServiceException;

@Service
@PropertySource("application.properties")
public class StudentService {

	private StudentDao studentDao;
	@Value("${group.size}")
	private int groupSize;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	@Transactional
	public void create(Student student) {
		if (isStudentValid(student)) {
			try {
				studentDao.create(student);
			} catch (DAOException e) {
				throw new ServiceException("Could not create student: " + student, e);
			}
		}
	}

	@Transactional
	public Optional<Student> findById(Long id) {
		try {
			return studentDao.findById(id);
		} catch (DAOException e) {
			throw new ServiceException("Could not get student by id: " + id, e);
		}
	}

	@Transactional
	public List<Student> getAll() {
		try {
			return studentDao.getAll();
		} catch (DAOException e) {
			throw new ServiceException("Could not get students", e);
		}
	}

	@Transactional
	public void update(Student student) {
		if (isStudentValid(student)) {
			try {
				studentDao.update(student);
			} catch (DAOException e) {
				throw new ServiceException("Could not update student: " + student, e);
			}
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			try {
				studentDao.deleteById(id);
			} catch (DAOException e) {
				throw new ServiceException("Could not delete student by id: " + id, e);
			}
		}
	}

	private boolean isStudentValid(Student student) {
		return student.getGender() != null
				&& student.getName() != null
				&& student.getSurname() != null
				&& !student.getName().isEmpty()
				&& !student.getSurname().isEmpty()
				&& isGroupSizeEnough(student);
	}

	private boolean isGroupSizeEnough(Student student) {
		return studentDao.getByGroup(student.getGroup())
				.stream()
				.count()
				< groupSize;
	}

	private boolean isPresentById(Long id) {
		return studentDao.findById(id).isPresent();
	}
}
