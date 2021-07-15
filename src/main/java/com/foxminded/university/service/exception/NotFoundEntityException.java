package com.foxminded.university.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@SuppressWarnings("serial")
public class NotFoundEntityException extends RuntimeException {

	public NotFoundEntityException() {
	}

	public NotFoundEntityException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundEntityException(String message) {
		super(message);
	}

	public NotFoundEntityException(Throwable cause) {
		super(cause);
	}
}
