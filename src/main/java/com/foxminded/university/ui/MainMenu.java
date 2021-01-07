package com.foxminded.university.ui;

import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class MainMenu {

	private static final String CHOICE_MESSAGE_FORMAT = "Please, choose the entity you want to interaction with "
			+ "or input 'EXIT' to leave the application:%n%s%n%s%n%s%n%s%n%s%n%s%n";
	private static final String STUDENT = "a. Student";
	private static final String TEACHER = "b. Teacher";
	private static final String GROUP = "c. Group";
	private static final String COURSE = "d. Course";
	private static final String ROOM = "e. Room";
	private static final String TIMEFRAME = "f. Timeframe";

	private Scanner scanner;
	private StudentMenu studentMenu;
	private TeacherMenu teacherMenu;
	private GroupMenu groupMenu;
	private CourseMenu courseMenu;
	private RoomMenu roomMenu;
	private TimeframeMenu timeframeMenu;

	public MainMenu(Scanner scanner, StudentMenu studentMenu, TeacherMenu teacherMenu, GroupMenu groupMenu,
			CourseMenu courseMenu, RoomMenu roomMenu, TimeframeMenu timeframeMenu) {
		this.scanner = scanner;
		this.studentMenu = studentMenu;
		this.teacherMenu = teacherMenu;
		this.groupMenu = groupMenu;
		this.courseMenu = courseMenu;
		this.roomMenu = roomMenu;
		this.timeframeMenu = timeframeMenu;
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
