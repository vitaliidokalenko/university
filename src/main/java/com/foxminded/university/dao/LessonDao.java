package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

public interface LessonDao extends GenericDao<Lesson> {

	public List<Lesson> getByGroupIdAndDate(Long groupId, LocalDate date);

	public List<Lesson> getByTimeframe(Timeframe timeframe);

	public List<Lesson> getByCourse(Course course);

	public List<Lesson> getByTeacherAndDate(Teacher teacher, LocalDate date);

	public List<Lesson> getByRoomAndDate(Room room, LocalDate date);
}
