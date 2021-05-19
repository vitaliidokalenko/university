package com.foxminded.university.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedQuery(
		name = "getAllTimeframes",
		query = "from Timeframe t")
@NamedQuery(
		name = "countTimeframes",
		query = "select count(t) from Timeframe t")
@NamedQuery(
		name = "findTimeframeBySequence",
		query = "from Timeframe t where t.sequence = :sequence")
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
	private int sequence;
	@Column(name = "start_time")
	private LocalTime startTime;
	@Column(name = "end_time")
	private LocalTime endTime;
}
