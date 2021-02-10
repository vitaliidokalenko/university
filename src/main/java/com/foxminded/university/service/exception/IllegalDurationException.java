package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class IllegalDurationException extends RuntimeException {

	public IllegalDurationException() {
	}

	public IllegalDurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalDurationException(String message) {
		super(message);
	}

	public IllegalDurationException(Throwable cause) {
		super(cause);
	}
}
