package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.HashSet;
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
}
