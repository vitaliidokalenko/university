package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
@SuppressWarnings("serial")
public class GroupOverflowException extends RuntimeException {

	public GroupOverflowException() {
	}

	public GroupOverflowException(String message, Throwable cause) {
		super(message, cause);
	}

	public GroupOverflowException(String message) {
		super(message);
	}

	public GroupOverflowException(Throwable cause) {
		super(cause);
	}
}
