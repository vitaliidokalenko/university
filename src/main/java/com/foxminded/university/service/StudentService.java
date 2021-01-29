package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;

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
			studentDao.create(student);
		}
	}

	@Transactional
	public Optional<Student> findById(Long id) {
		return studentDao.findById(id);
	}

	@Transactional
	public List<Student> getAll() {
		return studentDao.getAll();
	}

	@Transactional
	public void update(Student student) {
		if (isStudentValid(student)) {
			studentDao.update(student);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			studentDao.deleteById(id);
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
