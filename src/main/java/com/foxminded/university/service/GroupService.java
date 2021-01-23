package com.foxminded.university.service;

import java.util.List;

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
		groupDao.create(group);
	}

	@Transactional
	public Group findById(Long id) {
		Group group = groupDao.findById(id);
		group.setStudents(studentDao.getStudentsByGroup(group));
		return group;
	}

	@Transactional
	public List<Group> getAll() {
		return groupDao.getAll();
	}

	@Transactional
	public void update(Group group) {
		groupDao.update(group);
	}

	@Transactional
	public void deleteById(Long id) {
		groupDao.deleteById(id);
	}
}
