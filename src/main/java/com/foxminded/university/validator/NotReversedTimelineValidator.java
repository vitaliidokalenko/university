package com.foxminded.university.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.foxminded.university.model.Timeframe;
import com.foxminded.university.validator.annotation.NotReversedTimeline;

public class NotReversedTimelineValidator implements ConstraintValidator<NotReversedTimeline, Timeframe> {

	@Override
	public boolean isValid(Timeframe timeframe, ConstraintValidatorContext context) {
		return !timeframe.getStartTime().isAfter(timeframe.getEndTime());
	}
}
