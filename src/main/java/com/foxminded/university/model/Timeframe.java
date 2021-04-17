package com.foxminded.university.model;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timeframe {

	private Long id;
	private int sequence;
	private LocalTime startTime;
	private LocalTime endTime;
}
