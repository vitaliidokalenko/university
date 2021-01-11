package com.foxminded.university.service;

import java.util.List;

import com.foxminded.university.model.Lesson;

public interface LessonService extends GenericService<Lesson> {

	public void addGroupById(Long lessonId, Long groupId);

	public void removeGroupById(Long lessonId, Long groupId);

	public void setTeacherById(Long lessonId, Long teacherId);

	public void setCourseById(Long lessonId, Long courseId);

	public void setRoomById(Long lessonId, Long roomId);

	public void setTimeframeById(Long lessonId, Long timaframeId);

	public List<Lesson> getLessonsByGroupId(Long groupId);

	public List<Lesson> getLessonsByTimeframeId(Long timeframeId);

	public List<Lesson> getLessonsByCourseId(Long courseId);

	public List<Lesson> getLessonsByTeacherId(Long teacherId);

	public List<Lesson> getLessonsByRoomId(Long roomId);
}
