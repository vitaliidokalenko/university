package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class AlreadyExistsEntityException extends RuntimeException {

	public AlreadyExistsEntityException() {
		super();
	}

	public AlreadyExistsEntityException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyExistsEntityException(String message) {
		super(message);
	}

	public AlreadyExistsEntityException(Throwable cause) {
		super(cause);
	}

}
