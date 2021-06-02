package com.foxminded.university.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("university")
public class UniversityConfigProperties {

	private int maxGroupSize;
	private Duration lessonDuration;

}
