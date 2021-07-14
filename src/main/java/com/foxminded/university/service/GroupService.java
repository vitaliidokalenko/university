package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GroupService {

	private GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Transactional
	public void create(Group group) {
		log.debug("Creating group: {}", group);
		verify(group);
		groupDao.save(group);
	}

	@Transactional
	public Group findById(Long id) {
		log.debug("Finding group by id: {}", id);
		return groupDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find group by id: %d", id)));
	}

	@Transactional
	public List<Group> getAll() {
		log.debug("Getting groups");
		return groupDao.findAll();
	}

	@Transactional
	public Page<Group> getAllPage(Pageable pageable) {
		log.debug("Getting pageable groups");
		return groupDao.findAll(pageable);
	}

	@Transactional
	public void update(Group group) {
		log.debug("Updating group: {}", group);
		verify(group);
		groupDao.save(group);
	}

	@Transactional
	public void deleteById(Long id) {
		log.debug("Deleting group by id: {}", id);
		groupDao.delete(findById(id));
	}

	private void verify(Group group) {
		verifyNameIsUnique(group);
	}

	private void verifyNameIsUnique(Group group) {
		Optional<Group> groupByName = groupDao.findByName(group.getName());
		if (groupByName.isPresent() && !groupByName.get().getId().equals(group.getId())) {
			throw new NotUniqueNameException(format("The group with name %s already exists", group.getName()));
		}
	}
}
