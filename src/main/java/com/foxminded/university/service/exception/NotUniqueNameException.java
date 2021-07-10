package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@SuppressWarnings("serial")
public class NotUniqueNameException extends RuntimeException {

	public NotUniqueNameException() {
	}

	public NotUniqueNameException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotUniqueNameException(String message) {
		super(message);
	}

	public NotUniqueNameException(Throwable cause) {
		super(cause);
	}

}
