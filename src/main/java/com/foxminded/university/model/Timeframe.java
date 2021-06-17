package com.foxminded.university.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "timeframes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timeframe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Positive
	private int sequence;

	@NotNull
	@Column(name = "start_time")
	private LocalTime startTime;

	@NotNull
	@Column(name = "end_time")
	private LocalTime endTime;
}
