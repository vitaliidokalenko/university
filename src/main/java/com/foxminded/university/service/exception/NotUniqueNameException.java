package com.foxminded.university.service.exception;

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
