package com.foxminded.university.service.exception;

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
