package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
@SuppressWarnings("serial")
public class NotCompetentTeacherForCourseException extends RuntimeException {

	public NotCompetentTeacherForCourseException() {
	}

	public NotCompetentTeacherForCourseException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotCompetentTeacherForCourseException(String message) {
		super(message);
	}

	public NotCompetentTeacherForCourseException(Throwable cause) {
		super(cause);
	}
}
