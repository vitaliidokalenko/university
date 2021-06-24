package com.foxminded.university.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.foxminded.university.validator.NotWeekendValidator;

@Constraint(validatedBy = NotWeekendValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotWeekend {

	public String message() default "{com.foxminded.university.constraints.NotWeekend.message}";

	public Class<?>[] groups() default {};

	public Class<? extends Payload>[] payload() default {};
}