package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.IncompatibleRelationEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

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
		verify(student);
		studentDao.create(student);
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
		verify(student);
		studentDao.update(student);
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			studentDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(format("There is nothing to delete. Student with id: %d is absent", id));
		}
	}

	private void verify(Student student) {
		if (student.getName() == null) {
			throw new IllegalFieldEntityException("The name of the student is absent");
		} else if (student.getSurname() == null) {
			throw new IllegalFieldEntityException("The surname of the student is absent");
		} else if (student.getName().isEmpty()) {
			throw new IllegalFieldEntityException("The name of the student is empty");
		} else if (student.getSurname().isEmpty()) {
			throw new IllegalFieldEntityException("The surname of the student is empty");
		} else if (student.getGender() == null) {
			throw new IllegalFieldEntityException(
					format("Gender of the student %s %s is absent", student.getName(), student.getSurname()));
		} else if (!isGroupSizeEnough(student)) {
			throw new IncompatibleRelationEntityException(format(
					"The size of the group %s is %d students. It is not enough to include new student in",
					student.getGroup().getName(),
					groupSize));
		}
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
