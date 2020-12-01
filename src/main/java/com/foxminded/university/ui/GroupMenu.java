package com.foxminded.university.ui;

import java.util.Scanner;

import com.foxminded.university.model.Group;
import com.foxminded.university.repository.GroupRepository;

public class GroupMenu {

	private static final String CHOICE_MESSAGE_FORMAT = "%nPlease, input 'a'-'c' to select operation "
			+ "or 'MENU' to return to the main menu %n%s%n%s%n%s%n";
	private static final String A_COMMAND = "a. Add group to repository";
	private static final String B_COMMAND = "b. Remove group from repository";
	private static final String C_COMMAND = "c. Show list of groups";
	private static final String NAME_INQUIRY = "Please, insert group`s name:";
	private static final String ID_INQUIRY = "Please, insert group`s id:";
	private static final String PRINT_GROUPS_FORMAT = "id %d. %s%n";

	private final Scanner scanner;
	private final GroupRepository repository;

	public GroupMenu(Scanner scanner) {
		this.scanner = scanner;
		this.repository = new GroupRepository();
	}

	public void runMenu() {
		boolean exit = false;
		printMenu();
		while (!exit) {
			switch (getInputLetter()) {
			case "a":
				addGroup();
				printMenu();
				break;
			case "b":
				removeGroup();
				break;
			case "c":
				printGroups();
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

	private void addGroup() {
		System.out.println(NAME_INQUIRY);
		String name = scanner.nextLine();
		repository.create(new Group(name));
	}

	private void removeGroup() {
		System.out.println(ID_INQUIRY);
		int id = scanner.nextInt();
		repository.deleteById(id);
	}

	private void printGroups() {
		repository.getGroups()
				.forEach(g -> System.out.printf(PRINT_GROUPS_FORMAT, g.getId(), g.getName()));
	}
}
