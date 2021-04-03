package com.foxminded.university.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

	private Long id;
	private String name;
	private int capacity;

	public Room(String name) {
		this.name = name;
	}
}
