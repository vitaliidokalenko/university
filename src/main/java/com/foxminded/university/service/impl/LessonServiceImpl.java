package com.foxminded.university.service.impl;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.service.LessonService;

@Service
public class LessonServiceImpl implements LessonService {

	private LessonDao lessonDao;
	private GroupDao groupDao;
	private TeacherDao teacherDao;
	private CourseDao courseDao;
	private RoomDao roomDao;
	private TimeframeDao timaframeDao;

	public LessonServiceImpl(LessonDao lessonDao, GroupDao groupDao, TeacherDao teacherDao, CourseDao courseDao,
			RoomDao roomDao, TimeframeDao timeframeDao) {
		this.lessonDao = lessonDao;
		this.groupDao = groupDao;
		this.teacherDao = teacherDao;
		this.courseDao = courseDao;
		this.roomDao = roomDao;
		this.timaframeDao = timeframeDao;
	}

	@Override
	@Transactional
	public void create(Lesson lesson) {
		lessonDao.create(lesson);
	}

	@Override
	@Transactional
	public Lesson findById(Long id) {
		Lesson lesson = lessonDao.findById(id);
		lesson.setGroups(groupDao.getGroupsByLessonId(id).stream().collect(toSet()));
		return lesson;
	}

	@Override
	@Transactional
	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	@Override
	@Transactional
	public void update(Lesson lesson) {
		lessonDao.update(lesson);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		lessonDao.deleteById(id);
	}

	@Override
	@Transactional
	public void addGroupById(Long lessonId, Long groupId) {
		Lesson lesson = lessonDao.findById(lessonId);
		Set<Group> groups = groupDao.getGroupsByLessonId(lessonId).stream().collect(toSet());
		groups.add(groupDao.findById(groupId));
		lesson.setGroups(groups);
		lessonDao.update(lesson);
	}

	@Override
	@Transactional
	public void removeGroupById(Long lessonId, Long groupId) {
		Lesson lesson = lessonDao.findById(lessonId);
		Set<Group> groups = groupDao.getGroupsByLessonId(lessonId).stream().collect(toSet());
		groups.remove(groupDao.findById(groupId));
		lesson.setGroups(groups);
		lessonDao.update(lesson);
	}

	@Override
	@Transactional
	public void setTeacherById(Long lessonId, Long teacherId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.setTeacher(teacherDao.findById(teacherId));
		lessonDao.update(lesson);
	}

	@Override
	@Transactional
	public void setCourseById(Long lessonId, Long courseId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.setCourse(courseDao.findById(courseId));
		lessonDao.update(lesson);
	}

	@Override
	@Transactional
	public void setRoomById(Long lessonId, Long roomId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.setRoom(roomDao.findById(roomId));
		lessonDao.update(lesson);
	}

	@Override
	@Transactional
	public void setTimeframeById(Long lessonId, Long timaframeId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.setTimeframe(timaframeDao.findById(timaframeId));
		lessonDao.update(lesson);
	}

	@Override
	@Transactional
	public List<Lesson> getLessonsByGroupId(Long groupId) {
		return lessonDao.getLessonsByGroupId(groupId);
	}

	@Override
	@Transactional
	public List<Lesson> getLessonsByTimeframeId(Long timeframeId) {
		return lessonDao.getLessonsByTimeframe(timaframeDao.findById(timeframeId));
	}

	@Override
	@Transactional
	public List<Lesson> getLessonsByCourseId(Long courseId) {
		return lessonDao.getLessonsByCourse(courseDao.findById(courseId));
	}

	@Override
	@Transactional
	public List<Lesson> getLessonsByTeacherId(Long teacherId) {
		return lessonDao.getLessonsByTeacher(teacherDao.findById(teacherId));
	}

	@Override
	@Transactional
	public List<Lesson> getLessonsByRoomId(Long roomId) {
		return lessonDao.getLessonsByRoom(roomDao.findById(roomId));
	}

	@Override
	@Transactional
	public boolean existsById(Long id) {
		return Optional.of(lessonDao.findById(id)).isPresent();
	}
}
