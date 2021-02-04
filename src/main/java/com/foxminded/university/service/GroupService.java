package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.exception.DAOException;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.exception.ServiceException;

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
			try {
				groupDao.create(group);
			} catch (DAOException e) {
				throw new ServiceException("Could not create group: " + group, e);
			}
		}
	}

	@Transactional
	public Optional<Group> findById(Long id) {
		try {
			Optional<Group> group = groupDao.findById(id);
			if (group.isPresent()) {
				group.get().setStudents(studentDao.getByGroup(group.get()));
			}
			return group;
		} catch (DAOException e) {
			throw new ServiceException("Could not get group by id: " + id, e);
		}
	}

	@Transactional
	public List<Group> getAll() {
		try {
			return groupDao.getAll();
		} catch (DAOException e) {
			throw new ServiceException("Could not get groups", e);
		}
	}

	@Transactional
	public void update(Group group) {
		if (isGroupValid(group)) {
			try {
				groupDao.update(group);
			} catch (DAOException e) {
				throw new ServiceException("Could not update group: " + group, e);
			}
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			try {
				groupDao.deleteById(id);
			} catch (DAOException e) {
				throw new ServiceException("Could not delete group by id: " + id, e);
			}
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
