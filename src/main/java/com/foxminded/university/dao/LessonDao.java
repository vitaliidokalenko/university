package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

public interface LessonDao extends GenericDao<Lesson> {

	public List<Lesson> getLessonsByGroupIdAndDate(Long groupId, LocalDate date);

	public List<Lesson> getLessonsByTimeframe(Timeframe timeframe);

	public List<Lesson> getLessonsByCourse(Course course);

	public List<Lesson> getLessonsByTeacherAndDate(Teacher teacher, LocalDate date);

	public List<Lesson> getLessonsByRoomAndDate(Room room, LocalDate date);
}
