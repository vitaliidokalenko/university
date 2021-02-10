package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class IncompatibleDateException extends RuntimeException {

	public IncompatibleDateException() {
	}

	public IncompatibleDateException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncompatibleDateException(String message) {
		super(message);
	}

	public IncompatibleDateException(Throwable cause) {
		super(cause);
	}
}
