package com.foxminded.university.api.controller.exception;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error(ex.getMessage(), ex);
		Map<String, String> body = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(RuntimeException.class)
	public void handleRuntimeException(RuntimeException ex, HttpServletResponse response) throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
}
