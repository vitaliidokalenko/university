package com.foxminded.university.ui;

import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;

@Component
public class RoomMenu {

	private static final String CHOICE_MESSAGE_FORMAT = "%nPlease, input 'a'-'c' to select operation "
			+ "or 'MENU' to return to the main menu %n%s%n%s%n%s%n";
	private static final String A_COMMAND = "a. Add room to repository";
	private static final String B_COMMAND = "b. Remove room from repository";
	private static final String C_COMMAND = "c. Show list of rooms";
	private static final String NAME_INQUIRY = "Please, insert room`s name:";
	private static final String ID_INQUIRY = "Please, insert room`s id:";
	private static final String PRINT_ROOMS_FORMAT = "id %d. %s%n";

	private Scanner scanner;
	private RoomDao roomDao;

	public RoomMenu(Scanner scanner, RoomDao roomDao) {
		this.scanner = scanner;
		this.roomDao = roomDao;
	}

	public void runMenu() {
		boolean exit = false;
		printMenu();
		while (!exit) {
			switch (getInputLetter()) {
			case "a":
				addRoom();
				printMenu();
				break;
			case "b":
				removeRoom();
				break;
			case "c":
				printRooms();
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

	private void addRoom() {
		System.out.println(NAME_INQUIRY);
		String name = scanner.nextLine();
		roomDao.create(new Room(name));
	}

	private void removeRoom() {
		System.out.println(ID_INQUIRY);
		long id = scanner.nextInt();
		roomDao.deleteById(id);
	}

	private void printRooms() {
		roomDao.getAll().forEach(r -> System.out.printf(PRINT_ROOMS_FORMAT, r.getId(), r.getName()));
	}
}
