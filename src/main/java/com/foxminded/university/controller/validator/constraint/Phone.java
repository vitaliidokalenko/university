package com.foxminded.university.controller.validator.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$",
		message = "{com.foxminded.university.constraints.Phone.message}")
@Size(min = 6, max = 18)
@Constraint(validatedBy = {})
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {

	public String message() default "{com.foxminded.university.constraints.Phone.message}";

	public Class<?>[] groups() default {};

	public Class<? extends Payload>[] payload() default {};
}
