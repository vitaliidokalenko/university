package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class NotEnoughRoomCapacityException extends RuntimeException {

	public NotEnoughRoomCapacityException() {
	}

	public NotEnoughRoomCapacityException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughRoomCapacityException(String message) {
		super(message);
	}

	public NotEnoughRoomCapacityException(Throwable cause) {
		super(cause);
	}
}
