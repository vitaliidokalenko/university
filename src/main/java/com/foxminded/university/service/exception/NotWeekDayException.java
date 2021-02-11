package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class NotWeekDayException extends RuntimeException {

	public NotWeekDayException() {
	}

	public NotWeekDayException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotWeekDayException(String message) {
		super(message);
	}

	public NotWeekDayException(Throwable cause) {
		super(cause);
	}
}
