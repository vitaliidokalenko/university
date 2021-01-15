package com.foxminded.university.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

	private GroupDao groupDao;
	private StudentDao studentDao;

	public GroupServiceImpl(GroupDao groupDao, StudentDao studentDao) {
		this.groupDao = groupDao;
		this.studentDao = studentDao;
	}

	@Override
	@Transactional
	public void create(Group group) {
		groupDao.create(group);
	}

	@Override
	@Transactional
	public Group findById(Long id) {
		Group group = groupDao.findById(id);
		group.setStudents(studentDao.getStudentsByGroup(group));
		return group;
	}

	@Override
	@Transactional
	public List<Group> getAll() {
		return groupDao.getAll();
	}

	@Override
	@Transactional
	public void update(Group group) {
		groupDao.update(group);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		groupDao.deleteById(id);
	}

	@Override
	@Transactional
	public void addStudentById(Long groupId, Long studentId) {
		Group group = groupDao.findById(groupId);
		List<Student> students = studentDao.getStudentsByGroup(group);
		students.add(studentDao.findById(studentId));
		group.setStudents(students);
		groupDao.update(group);
	}

	@Override
	@Transactional
	public void removeStudentById(Long groupId, Long studentId) {
		Group group = groupDao.findById(groupId);
		List<Student> students = studentDao.getStudentsByGroup(group);
		students.remove(studentDao.findById(studentId));
		group.setStudents(students);
		groupDao.update(group);
	}

	@Override
	@Transactional
	public List<Group> getGroupsByLessonId(Long lessonId) {
		return groupDao.getGroupsByLessonId(lessonId);
	}
}
