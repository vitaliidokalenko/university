package com.foxminded.university.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.foxminded.university.validator.annotation.Age;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {

	private int min;
	private int max;

	@Override
	public void initialize(Age constraint) {
		this.min = constraint.min();
		this.max = constraint.max();
	}

	@Override
	public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
		int age = (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now());
		return age < max && age > min;
	}
}
