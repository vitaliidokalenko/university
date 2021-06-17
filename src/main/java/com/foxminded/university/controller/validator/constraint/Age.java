package com.foxminded.university.controller.validator.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.foxminded.university.controller.validator.AgeValidator;

@Constraint(validatedBy = AgeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Age {

	public String message() default "{com.foxminded.university.constraints.Age.message}";

	public Class<?>[] groups() default {};

	public Class<? extends Payload>[] payload() default {};

	int min() default 0;

	int max() default Integer.MAX_VALUE;
}