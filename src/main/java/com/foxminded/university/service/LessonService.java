package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

@Service
public class LessonService {

	private LessonDao lessonDao;
	private GroupDao groupDao;
	private TeacherDao teacherDao;
	private CourseDao courseDao;
	private RoomDao roomDao;
	private TimeframeDao timeframeDao;

	public LessonService(LessonDao lessonDao, GroupDao groupDao, TeacherDao teacherDao, CourseDao courseDao,
			RoomDao roomDao, TimeframeDao timeframeDao) {
		this.lessonDao = lessonDao;
		this.groupDao = groupDao;
		this.teacherDao = teacherDao;
		this.courseDao = courseDao;
		this.roomDao = roomDao;
		this.timeframeDao = timeframeDao;
	}

	@Transactional
	public void create(Lesson lesson) {
		lessonDao.create(lesson);
	}

	@Transactional
	public Lesson findById(Long id) {
		return lessonDao.findById(id);
	}

	@Transactional
	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	@Transactional
	public void update(Lesson lesson) {
		lessonDao.update(lesson);
	}

	@Transactional
	public void deleteById(Long id) {
		lessonDao.deleteById(id);
	}

	@Transactional
	public void addGroupById(Long lessonId, Long groupId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.getGroups().add(groupDao.findById(groupId));
		lessonDao.update(lesson);
	}

	@Transactional
	public void removeGroupById(Long lessonId, Long groupId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.getGroups().remove(groupDao.findById(groupId));
		lessonDao.update(lesson);
	}

	@Transactional
	public void setTeacherById(Long lessonId, Long teacherId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.setTeacher(teacherDao.findById(teacherId));
		lessonDao.update(lesson);
	}

	@Transactional
	public void setCourseById(Long lessonId, Long courseId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.setCourse(courseDao.findById(courseId));
		lessonDao.update(lesson);
	}

	@Transactional
	public void setRoomById(Long lessonId, Long roomId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.setRoom(roomDao.findById(roomId));
		lessonDao.update(lesson);
	}

	@Transactional
	public void setTimeframeById(Long lessonId, Long timaframeId) {
		Lesson lesson = lessonDao.findById(lessonId);
		lesson.setTimeframe(timeframeDao.findById(timaframeId));
		lessonDao.update(lesson);
	}

	@Transactional
	public List<Lesson> getLessonsByGroupId(Long groupId) {
		return lessonDao.getLessonsByGroupId(groupId);
	}

	@Transactional
	public List<Lesson> getLessonsByTimeframe(Timeframe timeframe) {
		return lessonDao.getLessonsByTimeframe(timeframe);
	}

	@Transactional
	public List<Lesson> getLessonsByCourse(Course course) {
		return lessonDao.getLessonsByCourse(course);
	}

	@Transactional
	public List<Lesson> getLessonsByTeacher(Teacher teacher) {
		return lessonDao.getLessonsByTeacher(teacher);
	}

	@Transactional
	public List<Lesson> getLessonsByRoom(Room room) {
		return lessonDao.getLessonsByRoom(room);
	}
}
