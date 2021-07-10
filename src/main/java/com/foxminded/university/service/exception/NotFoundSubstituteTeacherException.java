package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
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
