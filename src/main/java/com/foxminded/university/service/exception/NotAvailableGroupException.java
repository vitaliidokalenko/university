package com.foxminded.university.service.exception;

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
