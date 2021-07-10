package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
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
