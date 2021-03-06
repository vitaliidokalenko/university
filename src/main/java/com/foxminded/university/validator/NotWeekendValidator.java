package com.foxminded.university.validator;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.foxminded.university.validator.annotation.NotWeekend;

public class NotWeekendValidator implements ConstraintValidator<NotWeekend, LocalDate> {

	@Override
	public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
		DayOfWeek day = date.getDayOfWeek();
		return day != SATURDAY && day != SUNDAY;
	}
}
