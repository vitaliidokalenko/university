package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

public interface LessonService extends GenericService<Lesson> {

	public void addGroupById(Long lessonId, Long groupId);

	public void removeGroupById(Long lessonId, Long groupId);

	public void setTeacherById(Long lessonId, Long teacherId);

	public void setCourseById(Long lessonId, Long courseId);

	public void setRoomById(Long lessonId, Long roomId);

	public void setTimeframeById(Long lessonId, Long timaframeId);

	public List<Lesson> getLessonsByGroupId(Long groupId);

	public List<Lesson> getLessonsByTimeframe(Timeframe timeframe);

	public List<Lesson> getLessonsByCourse(Course course);

	public List<Lesson> getLessonsByTeacher(Teacher teacher);

	public List<Lesson> getLessonsByRoom(Room room);
}
