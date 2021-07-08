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

import com.foxminded.university.service.exception.GroupOverflowException;
import com.foxminded.university.service.exception.IncorrectDurationException;
import com.foxminded.university.service.exception.NotAvailableGroupException;
import com.foxminded.university.service.exception.NotAvailableRoomException;
import com.foxminded.university.service.exception.NotAvailableTeacherException;
import com.foxminded.university.service.exception.NotCompetentTeacherForCourseException;
import com.foxminded.university.service.exception.NotEnoughRoomCapacityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotFoundSubstituteTeacherException;
import com.foxminded.university.service.exception.NotSuitableRoomForCourseException;
import com.foxminded.university.service.exception.NotUniqueNameException;
import com.foxminded.university.service.exception.NotUniqueSequenceException;
import com.foxminded.university.service.exception.NotWeekDayException;

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

	@ExceptionHandler(NotFoundEntityException.class)
	public void handleNotFoundEntityException(Exception ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(GroupOverflowException.class)
	public void handleGroupOverflowException(GroupOverflowException ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(IncorrectDurationException.class)
	public void handleIncorrectDurationException(IncorrectDurationException ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(NotAvailableGroupException.class)
	public void handleNotAvailableExceptions(NotAvailableGroupException ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(NotAvailableRoomException.class)
	public void handleNotAvailableRoomException(NotAvailableRoomException ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(NotAvailableTeacherException.class)
	public void handleNotAvailableTeacherException(NotAvailableTeacherException ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(NotCompetentTeacherForCourseException.class)
	public void handleNotCompetentTeacherForCourseException(NotCompetentTeacherForCourseException ex,
			HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(NotEnoughRoomCapacityException.class)
	public void handleNotEnoughRoomCapacityException(NotEnoughRoomCapacityException ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(NotFoundSubstituteTeacherException.class)
	public void handleNotFoundSubstituteTeacherException(NotFoundSubstituteTeacherException ex,
			HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(NotSuitableRoomForCourseException.class)
	public void handleNotSuitableRoomForCourseException(NotSuitableRoomForCourseException ex,
			HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(NotUniqueNameException.class)
	public void handleNotUniqueNameException(NotUniqueNameException ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(NotUniqueSequenceException.class)
	public void handleNotUniqueSequenceException(NotUniqueSequenceException ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}

	@ExceptionHandler(NotWeekDayException.class)
	public void handleNotWeekDayException(NotWeekDayException ex, HttpServletResponse response)
			throws IOException {
		log.error(ex.getMessage(), ex);
		response.sendError(HttpStatus.CONFLICT.value());
	}
}
