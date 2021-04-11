package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class NotFoundSubstituteTeacherException extends RuntimeException {

	public NotFoundSubstituteTeacherException() {
	}

	public NotFoundSubstituteTeacherException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundSubstituteTeacherException(String message) {
		super(message);
	}

	public NotFoundSubstituteTeacherException(Throwable cause) {
		super(cause);
	}
}
