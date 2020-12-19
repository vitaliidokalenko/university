package com.foxminded.university.dao;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class ScriptRunner {

	private DataSource dataSource;

	public ScriptRunner(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void runScript(String sqlFile) {
		Resource resource = new ClassPathResource(sqlFile);
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
		databasePopulator.execute(dataSource);
	}
}
