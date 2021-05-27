package com.foxminded.university.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.foxminded.university.controller.formatter.CourseFormatter;
import com.foxminded.university.controller.formatter.GroupFormatter;
import com.foxminded.university.controller.formatter.RoomFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(new RoomFormatter());
		registry.addFormatter(new GroupFormatter());
		registry.addFormatter(new CourseFormatter());
	}
}