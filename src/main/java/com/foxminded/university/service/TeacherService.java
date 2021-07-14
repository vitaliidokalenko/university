package com.foxminded.university.service;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.exception.NotFoundEntityException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TeacherService {

	private TeacherDao teacherDao;

	public TeacherService(TeacherDao teacherDao) {
		this.teacherDao = teacherDao;
	}

	@Transactional
	public void create(Teacher teacher) {
		log.debug("Creating teacher: {}", teacher);
		teacherDao.save(teacher);
	}

	@Transactional
	public Teacher findById(Long id) {
		log.debug("Finding teacher by id: {}", id);
		return teacherDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find teacher by id: %d", id)));
	}

	@Transactional
	public List<Teacher> getAll() {
		log.debug("Getting teachers");
		return teacherDao.findAll();
	}

	@Transactional
	public Page<Teacher> getAllPage(Pageable pageable) {
		log.debug("Getting pageable teachers");
		return teacherDao.findAll(pageable);
	}

	@Transactional
	public void update(Teacher teacher) {
		log.debug("Updating teacher: {}", teacher);
		teacherDao.save(teacher);
	}

	@Transactional
	public void deleteById(Long id) {
		log.debug("Deleting teacher by id: {}", id);
		teacherDao.delete(findById(id));
	}

	@Transactional
	public Set<Teacher> getSubstituteTeachers(Teacher teacher) {
		log.debug("Getting substitutes for teacher: {}", teacher);
		return teacher.getCourses()
				.stream()
				.map(c -> teacherDao.getByCourses(c))
				.flatMap(List::stream)
				.filter(t -> !t.equals(teacher))
				.collect(toSet());
	}
}
