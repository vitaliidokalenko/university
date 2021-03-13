package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Group;

@SpringJUnitConfig(TestAppConfig.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcGroupDaoTest {

	private static final String GROUPS_TABLE_NAME = "groups";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcGroupDao groupDao;

	@Test
	@Sql("/dataGroups.sql")
	public void givenGroups_whenGetAll_thenGetRightListOfGroups() {
		Group group1 = new Group("AA-11");
		group1.setId(1L);
		Group group2 = new Group("BB-22");
		group2.setId(2L);
		Group group3 = new Group("CC-33");
		group3.setId(3L);
		List<Group> expected = Arrays.asList(group1, group2, group3);

		List<Group> actual = groupDao.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroup_whenCreate_thenGroupIsAddedToTable() {
		Group group = new Group("AA-22");
		int expectedRows = countRowsInTable(jdbcTemplate, GROUPS_TABLE_NAME) + 1;

		groupDao.create(group);

		int actualRows = countRowsInTable(jdbcTemplate, GROUPS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataGroups.sql")
	public void givenId_whenFindById_thenGetRightGroup() {
		Group expected = new Group("AA-11");
		expected.setId(1L);

		Group actual = groupDao.findById(1L).orElse(null);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataGroups.sql")
	public void givenUpdatedFields_whenUpdate_thenGroupsTableIsUpdated() {
		Group group = new Group("DD-44");
		group.setId(1L);
		int expectedRows = countRowsInTableWhere(jdbcTemplate, GROUPS_TABLE_NAME, "name = 'DD-44'") + 1;

		groupDao.update(group);

		int actualRows = countRowsInTableWhere(jdbcTemplate, GROUPS_TABLE_NAME, "name = 'DD-44'");
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/dataGroups.sql")
	public void givenGroupId_whenDeleteById_thenGroupIsDeleted() {
		int expectedRows = countRowsInTable(jdbcTemplate, GROUPS_TABLE_NAME) - 1;

		groupDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, GROUPS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql("/data.sql")
	public void givenLessonId_whenGetByLessonId_thenGetRightListOfGroups() {
		Group group1 = new Group("AA-11");
		group1.setId(1L);
		Group group2 = new Group("BB-22");
		group2.setId(2L);
		List<Group> expected = Arrays.asList(group1, group2);

		List<Group> actual = groupDao.getByLessonId(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataGroups.sql")
	public void givenId_whenFindByName_thenGetRightGroup() {
		Group expected = new Group("AA-11");
		expected.setId(1L);

		Group actual = groupDao.findByName("AA-11").orElse(null);

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataGroups.sql")
	public void whenCount_thenGetRightAmountOfGroups() {
		int expected = countRowsInTable(jdbcTemplate, GROUPS_TABLE_NAME);

		int actual = groupDao.count();

		assertEquals(expected, actual);
	}

	@Test
	@Sql("/dataGroups.sql")
	public void givenPageSize_whenGetAllPage_thenGetRightGroups() {
		Group group1 = new Group("AA-11");
		group1.setId(1L);
		Group group2 = new Group("BB-22");
		group2.setId(2L);
		List<Group> expected = Arrays.asList(group1, group2);
		int pageSize = 2;

		Page<Group> actual = groupDao.getAllPage(PageRequest.of(0, pageSize));

		assertEquals(expected, actual.getContent());
	}
}
