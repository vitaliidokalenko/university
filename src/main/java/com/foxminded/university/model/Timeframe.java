package com.foxminded.university.model;

import java.time.LocalTime;
import java.util.Objects;

public class Timeframe {

	private Long id;
	private int sequence;
	private LocalTime startTime;
	private LocalTime endTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
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

	@Override
	public int hashCode() {
		return Objects.hash(endTime, id, sequence, startTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Timeframe other = (Timeframe) obj;
		return Objects.equals(endTime, other.endTime)
				&& Objects.equals(id, other.id)
				&& sequence == other.sequence
				&& Objects.equals(startTime, other.startTime);
	}
}
