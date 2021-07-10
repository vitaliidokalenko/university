package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@SuppressWarnings("serial")
public class NotSuitableRoomForCourseException extends RuntimeException {

	public NotSuitableRoomForCourseException() {
	}

	public NotSuitableRoomForCourseException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotSuitableRoomForCourseException(String message) {
		super(message);
	}

	public NotSuitableRoomForCourseException(Throwable cause) {
		super(cause);
	}
}
