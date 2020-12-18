package com.foxminded.university.ui;

import java.util.Scanner;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Teacher;

public class TeacherMenu {

	private static final String CHOICE_MESSAGE_FORMAT = "%nPlease, input 'a'-'c' to select operation "
			+ "or 'MENU' to return to the main menu %n%s%n%s%n%s%n";
	private static final String A_COMMAND = "a. Add teacher to repository";
	private static final String B_COMMAND = "b. Remove teacher from repository";
	private static final String C_COMMAND = "c. Show list of teachers";
	private static final String NAME_INQUIRY = "Please, insert teacher`s name:";
	private static final String SURNAME_INQUIRY = "Please, insert teacher`s surname:";
	private static final String ID_INQUIRY = "Please, insert teacher`s id:";
	private static final String PRINT_TEACHERS_FORMAT = "id %d. %s %s%n";

	private final Scanner scanner;
	private final TeacherDao teacherDao;

	public TeacherMenu(Scanner scanner, TeacherDao teacherDao) {
		this.scanner = scanner;
		this.teacherDao = teacherDao;
	}

	public void runMenu() {
		boolean exit = false;
		printMenu();
		while (!exit) {
			switch (getInputLetter()) {
			case "a":
				addTeacher();
				printMenu();
				break;
			case "b":
				removeTeacher();
				break;
			case "c":
				printTeachers();
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

	private void addTeacher() {
		System.out.println(NAME_INQUIRY);
		String name = scanner.nextLine();
		System.out.println(SURNAME_INQUIRY);
		String surname = scanner.nextLine();
		teacherDao.create(new Teacher(name, surname));
	}

	private void removeTeacher() {
		System.out.println(ID_INQUIRY);
		long id = scanner.nextInt();
		teacherDao.deleteById(id);
	}

	private void printTeachers() {
		teacherDao.getAll()
				.forEach(t -> System.out.printf(PRINT_TEACHERS_FORMAT, t.getId(), t.getName(), t.getSurname()));
	}
}
