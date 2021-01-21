package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Lesson {

	private Long id;
	private Set<Group> groups = new HashSet<>();
	private Teacher teacher;
	private Course course;
	private Room room;
	private LocalDate date;
	private Timeframe timeframe;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Timeframe getTimeframe() {
		return timeframe;
	}

	public void setTimeframe(Timeframe timeframe) {
		this.timeframe = timeframe;
	}

	@Override
	public int hashCode() {
		return Objects.hash(course, date, groups, id, room, teacher, timeframe);
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
		Lesson other = (Lesson) obj;
		return Objects.equals(course, other.course) && Objects.equals(date, other.date)
				&& Objects.equals(groups, other.groups) && Objects.equals(id, other.id)
				&& Objects.equals(room, other.room) && Objects.equals(teacher, other.teacher)
				&& Objects.equals(timeframe, other.timeframe);
	}
}
