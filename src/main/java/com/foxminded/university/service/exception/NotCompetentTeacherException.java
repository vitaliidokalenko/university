package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class NotCompetentTeacherException extends RuntimeException {

	public NotCompetentTeacherException() {
	}

	public NotCompetentTeacherException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotCompetentTeacherException(String message) {
		super(message);
	}

	public NotCompetentTeacherException(Throwable cause) {
		super(cause);
	}
}
