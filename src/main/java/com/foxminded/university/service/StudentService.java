package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.exception.GroupOverflowException;
import com.foxminded.university.service.exception.NotFoundEntityException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentService {

	private StudentDao studentDao;
	private UniversityConfigProperties properties;

	public StudentService(StudentDao studentDao, UniversityConfigProperties properties) {
		this.studentDao = studentDao;
		this.properties = properties;
	}

	@Transactional
	public void create(Student student) {
		log.debug("Creating student: {}", student);
		verify(student);
		studentDao.save(student);
	}

	@Transactional
	public Student findById(Long id) {
		log.debug("Finding student by id: {}", id);
		return studentDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find student by id: %d", id)));
	}

	@Transactional
	public List<Student> getAll() {
		log.debug("Getting students");
		return studentDao.findAll();
	}

	@Transactional
	public Page<Student> getAllPage(Pageable pageable) {
		log.debug("Getting pageable students");
		return studentDao.findAll(pageable);
	}

	@Transactional
	public void update(Student student) {
		log.debug("Updating student: {}", student);
		if (findById(student.getId()) != null) {
			verify(student);
			studentDao.save(student);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		log.debug("Deleting student by id: {}", id);
		studentDao.delete(findById(id));
	}

	private void verify(Student student) {
		verifyGroupIsFull(student);
	}

	private void verifyGroupIsFull(Student student) {
		if (studentDao.getByGroup(student.getGroup()).stream().count() >= properties.getMaxGroupSize()) {
			throw new GroupOverflowException(format("The group %s is overflow (size = %d)",
					student.getGroup().getName(),
					properties.getMaxGroupSize()));
		}
	}
}
