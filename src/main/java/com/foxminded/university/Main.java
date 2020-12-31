package com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.ui.MainMenu;

public class Main {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		MainMenu menu = context.getBean(MainMenu.class);
		menu.runMenu();
		context.close();
	}
}
