package com.foxminded.university.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("com.foxminded.university")
@PropertySource("database.properties")
public class AppConfig {

	private static final String DRIVER = "driver";
	private static final String URL = "url";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	@Bean
	DataSource dataSource(Environment environment) {
		DriverManagerDataSource manager = new DriverManagerDataSource();
		manager.setDriverClassName(environment.getProperty(DRIVER));
		manager.setUrl(environment.getProperty(URL));
		manager.setUsername(environment.getProperty(USERNAME));
		manager.setPassword(environment.getProperty(PASSWORD));
		return manager;
	}
}
