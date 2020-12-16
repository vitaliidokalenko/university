package com.foxminded.university.model;

import java.time.LocalTime;

public class Timeframe {

	private Long id;
	private int sequance;
	private LocalTime startTime;
	private LocalTime endTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSequance() {
		return sequance;
	}

	public void setSequance(int sequance) {
		this.sequance = sequance;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
}
