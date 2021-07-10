package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@SuppressWarnings("serial")
public class NotAvailableGroupException extends RuntimeException {

	public NotAvailableGroupException() {
	}

	public NotAvailableGroupException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAvailableGroupException(String message) {
		super(message);
	}

	public NotAvailableGroupException(Throwable cause) {
		super(cause);
	}
}
