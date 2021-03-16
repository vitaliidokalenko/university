package com.foxminded.university.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@ComponentScan("com.foxminded.university.dao")
@PropertySource("classpath:application.properties")
public class AppConfig {

	private static final String SCHEMA = "schema.sql";

	@Value("${db.driver}")
	private String driver;
	@Value("${db.url}")
	private String url;
	@Value("${db.username}")
	private String username;
	@Value("${db.password}")
	private String password;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource manager = new DriverManagerDataSource();
		manager.setDriverClassName(driver);
		manager.setUrl(url);
		manager.setUsername(username);
		manager.setPassword(password);
		return manager;
	}

	@Bean
	public DataSourceInitializer dataSourceInitializer() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.addScript(new ClassPathResource(SCHEMA));
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource());
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		return dataSourceInitializer;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
}
