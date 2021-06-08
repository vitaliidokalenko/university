package com.foxminded.university.service;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.IncompleteEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Service
public class TeacherService {

	private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	@Transactional
	public void create(Teacher teacher) {
		logger.debug("Creating teacher: {}", teacher);
		verify(teacher);
		teacherDao.save(teacher);
	}

	@Transactional
	public Optional<Teacher> findById(Long id) {
		logger.debug("Finding teacher by id: {}", id);
		return teacherDao.findById(id);
	}

	@Transactional
	public List<Teacher> getAll() {
		logger.debug("Getting teachers");
		return teacherDao.findAll();
	}

	@Transactional
	public Page<Teacher> getAllPage(Pageable pageable) {
		logger.debug("Getting pageable teachers");
		return teacherDao.findAll(pageable);
	}

	@Transactional
	public void update(Teacher teacher) {
		logger.debug("Updating teacher: {}", teacher);
		verify(teacher);
		teacherDao.save(teacher);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting teacher by id: {}", id);
		teacherDao.delete(teacherDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find teacher by id: %d", id))));
	}

	@Transactional
	public Set<Teacher> getSubstituteTeachers(Teacher teacher) {
		logger.debug("Getting substitutes for teacher id: {}", teacher.getId());
		return teacher.getCourses()
				.stream()
				.map(c -> teacherDao.getByCourseId(c.getId()))
				.flatMap(List::stream)
				.filter(t -> !t.equals(teacher))
				.collect(toSet());
	}

	private void verify(Teacher teacher) {
		verifyFields(teacher);
	}

	private void verifyFields(Teacher teacher) {
		if (StringUtils.isEmpty(teacher.getName())) {
			throw new IllegalFieldEntityException("Empty teacher name");
		} else if (StringUtils.isEmpty(teacher.getSurname())) {
			throw new IllegalFieldEntityException("Empty teacher surname");
		} else if (teacher.getGender() == null) {
			throw new IllegalFieldEntityException("Empty teacher gender");
		} else if (teacher.getCourses().isEmpty()) {
			throw new IncompleteEntityException(format("No courses assigned to the teacher %s %s",
					teacher.getName(),
					teacher.getSurname()));
		}
	}
}
