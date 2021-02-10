package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class IllegalTimeLineException extends RuntimeException {

	public IllegalTimeLineException() {
	}

	public IllegalTimeLineException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalTimeLineException(String message) {
		super(message);
	}

	public IllegalTimeLineException(Throwable cause) {
		super(cause);
	}

}
