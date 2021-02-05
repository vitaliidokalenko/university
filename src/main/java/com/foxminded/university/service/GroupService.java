package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.exception.AlreadyExistsEntityException;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Service
public class GroupService {

	private GroupDao groupDao;
	private StudentDao studentDao;

	public GroupService(GroupDao groupDao, StudentDao studentDao) {
		this.groupDao = groupDao;
		this.studentDao = studentDao;
	}

	@Transactional
	public void create(Group group) {
		verify(group);
		groupDao.create(group);
	}

	@Transactional
	public Optional<Group> findById(Long id) {
		Optional<Group> group = groupDao.findById(id);
		if (group.isPresent()) {
			group.get().setStudents(studentDao.getByGroup(group.get()));
		}
		return group;
	}

	@Transactional
	public List<Group> getAll() {
		return groupDao.getAll();
	}

	@Transactional
	public void update(Group group) {
		verify(group);
		groupDao.update(group);
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			groupDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(format("There is nothing to delete. Group with id: %d is absent", id));
		}
	}

	private void verify(Group group) {
		if (group.getName() == null) {
			throw new IllegalFieldEntityException("The name of the group is absent");
		} else if (group.getName().isEmpty()) {
			throw new IllegalFieldEntityException("The name of the group is empty");
		} else if (!isNameUnique(group)) {
			throw new AlreadyExistsEntityException(format("The group with name %s already exists", group.getName()));
		}
	}

	private boolean isPresentById(Long id) {
		return groupDao.findById(id).isPresent();
	}

	private boolean isNameUnique(Group group) {
		Optional<Group> groupByName = groupDao.findByName(group.getName());
		if (groupByName.isPresent()) {
			return groupByName.get().getId().equals(group.getId());
		} else {
			return true;
		}
	}
}
