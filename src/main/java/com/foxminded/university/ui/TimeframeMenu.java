package com.foxminded.university.ui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Timeframe;

@Component
public class TimeframeMenu {

	private static final String CHOICE_MESSAGE_FORMAT = "%nPlease, input 'a'-'c' to select operation "
			+ "or 'MENU' to return to the main menu %n%s%n%s%n%s%n";
	private static final String A_COMMAND = "a. Add timeframe to repository";
	private static final String B_COMMAND = "b. Remove timeframe from repository";
	private static final String C_COMMAND = "c. Show list of timeframes";
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME;
	private static final String START_TIME_INQUIRY = "Please, insert timefram's start time in 'hh:mm' format:";
	private static final String END_TIME_INQUIRY = "Please, insert timeframe's end time in 'hh:mm' format:";
	private static final String SEQUANCE_INQUIRY = "Please, insert timeframe's sequance:";
	private static final String ID_INQUIRY = "Please, insert timeframe`s id:";
	private static final String PRINT_TIMEFRAMES_FORMAT = "id %d. %s - %s%n";

	private Scanner scanner;
	private TimeframeDao timeframeDao;

	public TimeframeMenu(Scanner scanner, TimeframeDao timeframeDao) {
		this.scanner = scanner;
		this.timeframeDao = timeframeDao;
	}

	public void runMenu() {
		boolean exit = false;
		printMenu();
		while (!exit) {
			switch (getInputLetter()) {
			case "a":
				addTimeframe();
				printMenu();
				break;
			case "b":
				removeTimeframe();
				break;
			case "c":
				printTimeframes();
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

	private void addTimeframe() {
		System.out.println(START_TIME_INQUIRY);
		String start = scanner.nextLine();
		LocalTime startTime = LocalTime.parse(start, TIME_FORMAT);
		System.out.println(END_TIME_INQUIRY);
		String end = scanner.nextLine();
		LocalTime endTime = LocalTime.parse(end, TIME_FORMAT);
		System.out.println(SEQUANCE_INQUIRY);
		int sequance = scanner.nextInt();
		Timeframe timeframe = new Timeframe();
		timeframe.setStartTime(startTime);
		timeframe.setEndTime(endTime);
		timeframe.setSequance(sequance);
		timeframeDao.create(timeframe);
	}

	private void removeTimeframe() {
		System.out.println(ID_INQUIRY);
		long id = scanner.nextInt();
		timeframeDao.deleteById(id);
	}

	private void printTimeframes() {
		timeframeDao.getAll()
				.forEach(t -> System.out.printf(PRINT_TIMEFRAMES_FORMAT, t.getId(), t.getStartTime(), t.getEndTime()));
	}
}
