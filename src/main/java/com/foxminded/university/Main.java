package com.foxminded.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.foxminded.university.config.UniversityConfigProperties;

@SpringBootApplication(
		exclude = {
				DataSourceTransactionManagerAutoConfiguration.class,
				HibernateJpaAutoConfiguration.class })
@EnableConfigurationProperties(UniversityConfigProperties.class)
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
