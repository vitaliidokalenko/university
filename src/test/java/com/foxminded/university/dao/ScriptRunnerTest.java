package com.foxminded.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.AppConfig;

@SpringJUnitConfig(AppConfig.class)
public class ScriptRunnerTest {

	private static final String GROUPS_TABLE_NAME = "groups";
	private static final String SQL_FILE = "testScript.sql";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ScriptRunner scriptRunner;

	@Test
	@Sql({ "/schema.sql", "/dataGroups.sql" })
	public void givenSqlScript_whenExecuteScript_thenScriptExecuted() {
		int expectedRows = countRowsInTable(jdbcTemplate, GROUPS_TABLE_NAME) - 1;

		scriptRunner.runScript(SQL_FILE);

		int actualRows = countRowsInTable(jdbcTemplate, GROUPS_TABLE_NAME);
		assertEquals(expectedRows, actualRows);

	}
}
