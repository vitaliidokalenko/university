package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@SuppressWarnings("serial")
public class NotUniqueSequenceException extends RuntimeException {

	public NotUniqueSequenceException() {
	}

	public NotUniqueSequenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotUniqueSequenceException(String message) {
		super(message);
	}

	public NotUniqueSequenceException(Throwable cause) {
		super(cause);
	}

}
