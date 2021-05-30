package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.exception.GroupOverflowException;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Service
public class StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

	private StudentDao studentDao;
	@Value("${university.max-group-size}")
	private int groupSize;

	public StudentService(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	@Transactional
	public void create(Student student) {
		logger.debug("Creating student: {}", student);
		verify(student);
		studentDao.create(student);
	}

	@Transactional
	public Optional<Student> findById(Long id) {
		logger.debug("Finding student by id: {}", id);
		return studentDao.findById(id);
	}

	@Transactional
	public List<Student> getAll() {
		logger.debug("Getting students");
		return studentDao.getAll();
	}

	@Transactional
	public Page<Student> getAllPage(Pageable pageable) {
		logger.debug("Getting pageable students");
		return studentDao.getAllPage(pageable);
	}

	@Transactional
	public void update(Student student) {
		logger.debug("Updating student: {}", student);
		verify(student);
		studentDao.update(student);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting student by id: {}", id);
		studentDao.delete(studentDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find student by id: %d", id))));
	}

	private void verify(Student student) {
		verifyFields(student);
		verifyGroupIsFull(student);
	}

	private void verifyFields(Student student) {
		if (StringUtils.isEmpty(student.getName())) {
			throw new IllegalFieldEntityException("Empty student name");
		} else if (StringUtils.isEmpty(student.getSurname())) {
			throw new IllegalFieldEntityException("Empty student surname");
		} else if (student.getGender() == null) {
			throw new IllegalFieldEntityException("Empty student gender");
		}
	}

	private void verifyGroupIsFull(Student student) {
		if (studentDao.getByGroup(student.getGroup()).stream().count() >= groupSize) {
			throw new GroupOverflowException(format("The group %s is overflow (size = %d)",
					student.getGroup().getName(),
					groupSize));
		}
	}
}
