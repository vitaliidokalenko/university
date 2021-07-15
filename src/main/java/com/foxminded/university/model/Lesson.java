package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foxminded.university.validator.annotation.NotWeekend;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lessons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@ManyToMany
	@JoinTable(name = "lessons_groups",
			joinColumns = @JoinColumn(name = "lesson_id"),
			inverseJoinColumns = @JoinColumn(name = "group_id"))
	@JsonIgnoreProperties("students")
	private Set<Group> groups = new HashSet<>();

	@NotNull
	@ManyToOne
	@JsonIgnoreProperties("courses")
	private Teacher teacher;

	@NotNull
	@ManyToOne
	@JsonIgnoreProperties("rooms")
	private Course course;

	@NotNull
	@ManyToOne
	private Room room;

	@NotNull
	@FutureOrPresent
	@NotWeekend
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;

	@NotNull
	@ManyToOne
	private Timeframe timeframe;
}
