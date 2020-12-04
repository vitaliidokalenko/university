package com.foxminded.university.repository;

import java.util.ArrayList;
import java.util.List;

import com.foxminded.university.model.Lesson;

public class LessonRepository {

	List<Lesson> lessons = new ArrayList<>();

	public void create(Lesson lesson) {
		lessons.add(lesson);
	}

	public void delete(Lesson lesson) {
		lessons.remove(lesson);
	}

	public List<Lesson> getLessons() {
		return lessons;
	}
}
