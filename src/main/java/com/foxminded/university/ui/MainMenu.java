package com.foxminded.university.ui;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.TimeframeDao;

public class MainMenu {

	private static final String CHOICE_MESSAGE_FORMAT = "Please, choose the entity you want to interaction with "
			+ "or input 'EXIT' to leave the application:%n%s%n%s%n%s%n%s%n%s%n%s%n";
	private static final String STUDENT = "a. Student";
	private static final String TEACHER = "b. Teacher";
	private static final String GROUP = "c. Group";
	private static final String COURSE = "d. Course";
	private static final String ROOM = "e. Room";
	private static final String TIMEFRAME = "f. Timeframe";

	private final Scanner scanner;
	private final StudentMenu studentMenu;
	private final TeacherMenu teacherMenu;
	private final GroupMenu groupMenu;
	private final CourseMenu courseMenu;
	private final RoomMenu roomMenu;
	private final TimeframeMenu timeframeMenu;

	public MainMenu(AnnotationConfigApplicationContext context) {
		scanner = new Scanner(System.in);
		studentMenu = new StudentMenu(scanner, context.getBean(StudentDao.class));
		teacherMenu = new TeacherMenu(scanner, context.getBean(TeacherDao.class));
		groupMenu = new GroupMenu(scanner, context.getBean(GroupDao.class));
		courseMenu = new CourseMenu(scanner, context.getBean(CourseDao.class));
		roomMenu = new RoomMenu(scanner, context.getBean(RoomDao.class));
		timeframeMenu = new TimeframeMenu(scanner, context.getBean(TimeframeDao.class));
	}

	public void runMenu() {
		boolean exit = false;
		printMenu();
		while (!exit) {
			switch (getInputLetter()) {
			case "a":
				studentMenu.runMenu();
				printMenu();
				break;
			case "b":
				teacherMenu.runMenu();
				printMenu();
				break;
			case "c":
				groupMenu.runMenu();
				printMenu();
				break;
			case "d":
				courseMenu.runMenu();
				printMenu();
				break;
			case "e":
				roomMenu.runMenu();
				printMenu();
				break;
			case "f":
				timeframeMenu.runMenu();
				printMenu();
				break;
			case "EXIT":
				exit = true;
				break;
			default:
				printMenu();
			}
		}
		scanner.close();
	}

	private void printMenu() {
		System.out.printf(CHOICE_MESSAGE_FORMAT, STUDENT, TEACHER, GROUP, COURSE, ROOM, TIMEFRAME);
	}

	private String getInputLetter() {
		return scanner.nextLine();
	}
}
