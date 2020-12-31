package com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.foxminded.university.config.AppConfig;
import com.foxminded.university.dao.ScriptRunner;
import com.foxminded.university.ui.MainMenu;

public class Main {

	public static void main(String[] args) {

		String schema = "schema.sql";

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		ScriptRunner scriptRunner = context.getBean(ScriptRunner.class);
		scriptRunner.runScript(schema);
		MainMenu menu = context.getBean(MainMenu.class);
		menu.runMenu();
		context.close();
	}
}
