package com.foxminded.university.controller.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(HttpServletRequest request, Exception e) {
		logger.error("Request: {} raised {}", request.getRequestURL(), e);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exception", e.getClass().getSimpleName());
		modelAndView.addObject("message", e.getMessage());
		modelAndView.setViewName("error");
		return modelAndView;
	}
}
