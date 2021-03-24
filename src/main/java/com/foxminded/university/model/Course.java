package com.foxminded.university.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Course {

	private Long id;
	private String name;
	private String description;
	private Set<Room> rooms = new HashSet<>();

	public Course() {

	}

	public Course(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Set<Room> rooms) {
		this.rooms = rooms;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, name, rooms);
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
		Course other = (Course) obj;
		return Objects.equals(description, other.description)
				&& Objects.equals(id, other.id)
				&& Objects.equals(name, other.name)
				&& Objects.equals(rooms, other.rooms);
	}
}
