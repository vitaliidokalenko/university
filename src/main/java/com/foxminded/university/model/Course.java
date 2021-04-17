package com.foxminded.university.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

	private Long id;
	private String name;
	private String description;
	private Set<Room> rooms = new HashSet<>();

	public Course(String name) {
		this.name = name;
	}
}
