package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

public interface LessonDao extends GenericDao<Lesson> {

	public List<Lesson> getLessonsByGroupId(Long groupId);

	public List<Lesson> getLessonsByTimeframe(Timeframe timeframe);

	public List<Lesson> getLessonsByCourse(Course course);

	public List<Lesson> getLessonsByTeacher(Teacher teacher);

	public List<Lesson> getLessonsByRoom(Room room);
}
