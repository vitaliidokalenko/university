package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class IllegalFieldEntityException extends RuntimeException {

	public IllegalFieldEntityException() {
	}

	public IllegalFieldEntityException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalFieldEntityException(String message) {
		super(message);
	}

	public IllegalFieldEntityException(Throwable cause) {
		super(cause);
	}
}
