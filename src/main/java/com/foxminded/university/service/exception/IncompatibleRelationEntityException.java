package com.foxminded.university.service.exception;

@SuppressWarnings("serial")
public class IncompatibleRelationEntityException extends RuntimeException {

	public IncompatibleRelationEntityException() {
		super();
	}

	public IncompatibleRelationEntityException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncompatibleRelationEntityException(String message) {
		super(message);
	}

	public IncompatibleRelationEntityException(Throwable cause) {
		super(cause);
	}
}
