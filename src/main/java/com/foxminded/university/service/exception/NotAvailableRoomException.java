package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class NotAvailableRoomException extends RuntimeException {

	public NotAvailableRoomException() {
	}

	public NotAvailableRoomException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAvailableRoomException(String message) {
		super(message);
	}

	public NotAvailableRoomException(Throwable cause) {
		super(cause);
	}
}
