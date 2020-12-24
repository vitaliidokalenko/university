package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.dao.jdbc.JdbcGroupDao;
import com.foxminded.university.model.Group;

@SpringJUnitConfig(AppConfig.class)
public class JdbcGroupDaoTest {

	private static final String GROUPS_TABLE_NAME = "groups";
	
	private JdbcTemplate jdbcTemplate;
	private JdbcGroupDao groupDao;

	@Autowired
	public JdbcGroupDaoTest(DataSource dataSource, JdbcGroupDao groupDao) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.groupDao = groupDao;
	}

	@Test
	@Sql({ "/schema.sql", "/dataGroups.sql" })
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
	@Sql("/schema.sql")
	public void givenGroup_whenCreate_thenGroupIsAddedToTable() {
		Group expected = new Group("AA-22");

		groupDao.create(expected);

		Group actual = groupDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataGroups.sql" })
	public void givenId_whenFindById_thenGetRightGroup() {
		Group expected = new Group("AA-11");
		expected.setId(1L);

		Group actual = groupDao.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataGroups.sql" })
	public void givenUpdatedFields_whenUpdate_thenGetRightGroup() {
		Group expected = new Group("DD-44");
		expected.setId(1L);

		groupDao.update(expected);

		Group actual = groupDao.getAll().get(0);
		assertEquals(expected, actual);
	}

	@Test
	@Sql({ "/schema.sql", "/dataGroups.sql" })
	public void givenGroupId_whenDeleteById_thenGroupIsDeleted() {
		int expectedRows = 2;

		groupDao.deleteById(1L);

		int actualRows = countRowsInTable(jdbcTemplate, GROUPS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);
	}

	@Test
	@Sql({ "/schema.sql", "/data.sql" })
	public void givenLessonId_whenGetGroupsByLessonId_thenGetRightListOfGroups() {
		Group group1 = new Group("AA-11");
		group1.setId(1L);
		Group group2 = new Group("BB-22");
		group2.setId(2L);
		List<Group> expected = Arrays.asList(group1, group2);

		List<Group> actual = groupDao.getGroupsByLessonId(1L);

		assertEquals(expected, actual);
	}
}
