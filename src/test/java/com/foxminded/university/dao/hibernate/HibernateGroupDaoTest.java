package com.foxminded.university.dao.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.model.Group;

@SpringJUnitConfig(TestAppConfig.class)
@Transactional
public class HibernateGroupDaoTest {

	@Autowired
	HibernateTemplate template;
	@Autowired
	HibernateGroupDao groupDao;

	@Test
	public void whenGetAll_thenGetRightListOfGroups() {
		List<Group> expected = template.loadAll(Group.class);

		List<Group> actual = groupDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenNewGroup_whenCreate_thenCreated() {
		Group expected = Group.builder().name("EE-55").build();

		groupDao.create(expected);

		Group actual = template.get(Group.class, 5L);
		assertEquals(expected, actual);
	}

	@Test
	public void givenId_whenFindById_thenGetRightGroup() {
		Optional<Group> expected = Optional.of(template.get(Group.class, 1L));

		Optional<Group> actual = groupDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenWrongId_whenFindById_thenGetEmptyOptional() {
		Optional<Group> expected = Optional.empty();

		Optional<Group> actual = groupDao.findById(10L);

		assertEquals(expected, actual);
	}

	@Test
	public void givenUpdatedFields_whenUpdate_thenGroupUpdated() {
		String expectedName = "Updated Name";
		Group group = template.get(Group.class, 1L);
		group.setName(expectedName);

		groupDao.update(group);

		assertEquals(expectedName, template.get(Group.class, 1L).getName());
	}

	@Test
	public void givenUpdatedStudents_whenUpdate_thenGroupsStudentsUpdated() {
		Group group = template.get(Group.class, 1L);
		group.getStudents().clear();

		groupDao.update(group);

		assertTrue(template.get(Group.class, 1L).getStudents().isEmpty());
	}

	@Test
	public void givenGroup_whenDelete_thenDeleted() {
		Group group = template.get(Group.class, 4L);

		groupDao.delete(group);

		assertNull(template.get(Group.class, 4L));
	}

	@Test
	public void whenCount_thenGetRightAmountOfGroups() {
		long expected = template.loadAll(Group.class).size();

		long actual = groupDao.count();

		assertEquals(expected, actual);
	}

	@Test
	public void givenPageSize_whenGetAllPage_thenGetRightGroups() {
		List<Group> expected = template.loadAll(Group.class).subList(0, 2);
		int pageSize = 2;

		Page<Group> actual = groupDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}

	@Test
	public void givenName_whenFindByName_thenGetRightGroup() {
		Optional<Group> expected = Optional.of(template.get(Group.class, 1L));

		Optional<Group> actual = groupDao.findByName("AA-11");

		assertEquals(expected, actual);
	}

	@Test
	public void givenLessonId_whenGetByLessonId_thenGetRightListOfGroups() {
		List<Group> expected = List.of(template.get(Group.class, 1L), template.get(Group.class, 2L));

		List<Group> actual = groupDao.getByLessonId(1L);

		assertEquals(expected, actual);
	}
}
