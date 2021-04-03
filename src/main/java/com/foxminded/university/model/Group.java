package com.foxminded.university.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

	private Long id;
	private String name;
	private List<Student> students;

	public Group(String name) {
		this.name = name;
	}
}
