package com.foxminded.university.service.exception;

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
