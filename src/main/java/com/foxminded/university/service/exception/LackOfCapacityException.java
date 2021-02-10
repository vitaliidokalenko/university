package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class LackOfCapacityException extends RuntimeException {

	public LackOfCapacityException() {
	}

	public LackOfCapacityException(String message, Throwable cause) {
		super(message, cause);
	}

	public LackOfCapacityException(String message) {
		super(message);
	}

	public LackOfCapacityException(Throwable cause) {
		super(cause);
	}
}
