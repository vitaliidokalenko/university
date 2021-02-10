package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class IncompatibleRoomException extends RuntimeException {

	public IncompatibleRoomException() {
	}

	public IncompatibleRoomException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncompatibleRoomException(String message) {
		super(message);
	}

	public IncompatibleRoomException(Throwable cause) {
		super(cause);
	}
}
