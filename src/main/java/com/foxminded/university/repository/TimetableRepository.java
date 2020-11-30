package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Timetable;

public class TimetableRepository {

	List<Timetable> timetables = new ArrayList<>();

	public void create(Timetable timetable) {
		timetables.add(timetable);
	}

	public void delete(Timetable timetable) {
		timetables.remove(timetable);
	}

	public List<Timetable> getTimetables() {
		return timetables;
	}
}
