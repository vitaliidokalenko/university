package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@SuppressWarnings("serial")
public class IncorrectDurationException extends RuntimeException {

	public IncorrectDurationException() {
	}

	public IncorrectDurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectDurationException(String message) {
		super(message);
	}

	public IncorrectDurationException(Throwable cause) {
		super(cause);
	}
}
