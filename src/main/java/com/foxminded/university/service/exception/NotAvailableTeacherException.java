package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@SuppressWarnings("serial")
public class NotAvailableTeacherException extends RuntimeException {

	public NotAvailableTeacherException() {
	}

	public NotAvailableTeacherException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAvailableTeacherException(String message) {
		super(message);
	}

	public NotAvailableTeacherException(Throwable cause) {
		super(cause);
	}
}
