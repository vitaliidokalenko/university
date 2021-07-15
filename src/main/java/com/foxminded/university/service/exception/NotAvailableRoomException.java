package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
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
