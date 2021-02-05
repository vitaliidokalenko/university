package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class IncompleteEntityException extends RuntimeException {

	public IncompleteEntityException() {
		super();
	}

	public IncompleteEntityException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncompleteEntityException(String message) {
		super(message);
	}

	public IncompleteEntityException(Throwable cause) {
		super(cause);
	}
}
