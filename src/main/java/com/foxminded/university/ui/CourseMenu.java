package com.foxminded.university.ui;

import java.util.Scanner;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;

public class CourseMenu {

	private static final String CHOICE_MESSAGE_FORMAT = "%nPlease, input 'a'-'c' to select operation "
			+ "or 'MENU' to return to the main menu %n%s%n%s%n%s%n";
	private static final String A_COMMAND = "a. Add course to repository";
	private static final String B_COMMAND = "b. Remove course from repository";
	private static final String C_COMMAND = "c. Show list of courses";
	private static final String NAME_INQUIRY = "Please, insert course`s name:";
	private static final String ID_INQUIRY = "Please, insert course`s id:";
	private static final String PRINT_COURSES_FORMAT = "id %d. %s%n";

	private final Scanner scanner;
	private final CourseDao courseDao;

	public CourseMenu(Scanner scanner, CourseDao courseDao) {
		this.scanner = scanner;
		this.courseDao = courseDao;
	}

	public void runMenu() {
		boolean exit = false;
		printMenu();
		while (!exit) {
			switch (getInputLetter()) {
			case "a":
				addCourse();
				printMenu();
				break;
			case "b":
				removeCourse();
				break;
			case "c":
				printCourses();
				printMenu();
				break;
			case "MENU":
				exit = true;
				break;
			default:
				printMenu();
			}
		}
	}

	private void printMenu() {
		System.out.printf(CHOICE_MESSAGE_FORMAT, A_COMMAND, B_COMMAND, C_COMMAND);
	}

	private String getInputLetter() {
		return scanner.nextLine();
	}

	private void addCourse() {
		System.out.println(NAME_INQUIRY);
		String name = scanner.nextLine();
		courseDao.create(new Course(name));
	}

	private void removeCourse() {
		System.out.println(ID_INQUIRY);
		long id = scanner.nextInt();
		courseDao.deleteById(id);
	}

	private void printCourses() {
		courseDao.getAll().forEach(c -> System.out.printf(PRINT_COURSES_FORMAT, c.getId(), c.getName()));
	}
}
