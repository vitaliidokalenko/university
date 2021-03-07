package com.foxminded.university.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlingController {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(HttpServletRequest request, Exception e) {
		logger.error("Request: {} raised {}", request.getRequestURL(), e);
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e.getClass().getSimpleName());
		mav.addObject("message", e.getMessage());
		mav.setViewName("error");
		return mav;
	}
}
