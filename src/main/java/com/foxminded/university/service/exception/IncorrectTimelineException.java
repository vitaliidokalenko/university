package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class IncorrectTimelineException extends RuntimeException {

	public IncorrectTimelineException() {
	}

	public IncorrectTimelineException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectTimelineException(String message) {
		super(message);
	}

	public IncorrectTimelineException(Throwable cause) {
		super(cause);
	}

}
