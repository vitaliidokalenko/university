package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Group;

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
		if (isGroupValid(group)) {
			groupDao.create(group);
		}
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
		if (isGroupValid(group)) {
			groupDao.update(group);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			groupDao.deleteById(id);
		}
	}

	private boolean isGroupValid(Group group) {
		return group.getName() != null
				&& !group.getName().isEmpty()
				&& isNameUnique(group);
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
