package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
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
