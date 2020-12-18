package com.foxminded.university.ui;

import java.util.Scanner;

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Gender;
import com.foxminded.university.model.Student;

public class StudentMenu {

	private static final String CHOICE_MESSAGE_FORMAT = "%nPlease, input 'a'-'c' to select operation "
			+ "or 'MENU' to return to the main menu %n%s%n%s%n%s%n";
	private static final String A_COMMAND = "a. Add student to repository";
	private static final String B_COMMAND = "b. Remove student from repository";
	private static final String C_COMMAND = "c. Show list of students";
	private static final String NAME_INQUIRY = "Please, insert student's name:";
	private static final String SURNAME_INQUIRY = "Please, insert student's surname:";
	private static final String GENDER_ENQUIRY_FORMAT = "Please, choose student's gender%n%s%n%s%n";
	private static final String MALE_GENDER = "a. MALE";
	private static final String FEMALE_GENDER = "b. FEMALE";
	private static final String ID_INQUIRY = "Please, insert student's id:";
	private static final String PRINT_STUDENTS_FORMAT = "id %d. %s %s%n";

	private final Scanner scanner;
	private final StudentDao studentDao;

	public StudentMenu(Scanner scanner, StudentDao studentDao) {
		this.scanner = scanner;
		this.studentDao = studentDao;
	}

	public void runMenu() {
		boolean exit = false;
		printMenu();
		while (!exit) {
			switch (getInputLetter()) {
			case "a":
				addStudent();
				printMenu();
				break;
			case "b":
				removeStudent();
				break;
			case "c":
				printStudents();
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

	private void addStudent() {
		System.out.println(NAME_INQUIRY);
		String name = scanner.nextLine();
		System.out.println(SURNAME_INQUIRY);
		String surname = scanner.nextLine();
		Student student = new Student(name, surname);
		System.out.printf(GENDER_ENQUIRY_FORMAT, MALE_GENDER, FEMALE_GENDER);
		String genderChoice = scanner.nextLine();
		if (genderChoice.equals("a")) {
			student.setGender(Gender.MALE);
		} else if (genderChoice.equals("b")) {
			student.setGender(Gender.FEMALE);
		}
		studentDao.create(student);
	}

	private void printStudents() {
		studentDao.getAll()
				.forEach(s -> System.out.printf(PRINT_STUDENTS_FORMAT, s.getId(), s.getName(), s.getSurname()));
	}

	private void removeStudent() {
		System.out.println(ID_INQUIRY);
		long id = scanner.nextInt();
		studentDao.deleteById(id);
	}
}
