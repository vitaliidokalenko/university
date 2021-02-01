package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

public interface LessonDao extends GenericDao<Lesson> {

	public Optional<Lesson> getByGroupIdAndDateAndTimeframe(Long groupId, LocalDate date, Timeframe timeframe);

	public List<Lesson> getByTimeframe(Timeframe timeframe);

	public List<Lesson> getByCourse(Course course);

	public Optional<Lesson> getByTeacherAndDateAndTimeframe(Teacher teacher, LocalDate date, Timeframe timeframe);

	public Optional<Lesson> getByRoomAndDateAndTimeframe(Room room, LocalDate date, Timeframe timeframe);
}
