package com.foxminded.university.service;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

@Service
public class GroupService {

	private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

	private GroupDao groupDao;

	public GroupService(GroupDao groupDao) {
		this.groupDao = groupDao;
	}

	@Transactional
	public void create(Group group) {
		logger.debug("Creating group: {}", group);
		verify(group);
		groupDao.save(group);
	}

	@Transactional
	public Optional<Group> findById(Long id) {
		logger.debug("Finding group by id: {}", id);
		return groupDao.findById(id);
	}

	@Transactional
	public List<Group> getAll() {
		logger.debug("Getting groups");
		return groupDao.findAll();
	}

	@Transactional
	public Page<Group> getAllPage(Pageable pageable) {
		logger.debug("Getting pageable groups");
		return groupDao.findAll(pageable);
	}

	@Transactional
	public void update(Group group) {
		logger.debug("Updating group: {}", group);
		verify(group);
		groupDao.save(group);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting group by id: {}", id);
		groupDao.delete(groupDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find group by id: %d", id))));
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
