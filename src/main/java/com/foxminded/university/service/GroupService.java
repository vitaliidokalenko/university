package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

@Service
public class GroupService {

	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

	private GroupDao groupDao;
	private StudentDao studentDao;

	public GroupService(GroupDao groupDao, StudentDao studentDao) {
		this.groupDao = groupDao;
		this.studentDao = studentDao;
	}

	@Transactional
	public void create(Group group) {
		logger.debug("Creating group: {}", group);
		verify(group);
		groupDao.create(group);
	}

	@Transactional
	public Optional<Group> findById(Long id) {
		logger.debug("Finding group by id: {}", id);
		Optional<Group> group = groupDao.findById(id);
		if (group.isPresent()) {
			group.get().setStudents(studentDao.getByGroup(group.get()));
		}
		return group;
	}

	@Transactional
	public List<Group> getAll() {
		logger.debug("Getting groups");
		return groupDao.getAll();
	}

	@Transactional
	public void update(Group group) {
		logger.debug("Updating group: {}", group);
		verify(group);
		groupDao.update(group);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting group by id: {}", id);
		if (groupDao.findById(id).isPresent()) {
			groupDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(format("Cannot find group by id: %d", id));
		}
	}

	private void verify(Group group) {
		verifyFields(group);
		verifyNameIsUnique(group);
	}

	private void verifyFields(Group group) {
		if (StringUtils.isEmpty(group.getName())) {
			throw new IllegalFieldEntityException("Empty group name");
		}
	}

	private void verifyNameIsUnique(Group group) {
		Optional<Group> groupByName = groupDao.findByName(group.getName());
		if (groupByName.isPresent() && !groupByName.get().getId().equals(group.getId())) {
			throw new NotUniqueNameException(format("The group with name %s already exists", group.getName()));
		}
	}
}
