package com.foxminded.university.service;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Lesson;

@Service
public class LessonService {

	private LessonDao lessonDao;
	private StudentDao studentDao;

	public LessonService(LessonDao lessonDao, StudentDao studentDao) {
		this.lessonDao = lessonDao;
		this.studentDao = studentDao;
	}

	@Transactional
	public void create(Lesson lesson) {
		if (isLessonValid(lesson)) {
			lessonDao.create(lesson);
		}
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
		if (isLessonValid(lesson)) {
			lessonDao.update(lesson);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			lessonDao.deleteById(id);
		}
	}

	private boolean isLessonValid(Lesson lesson) {
		return lesson.getCourse() != null
				&& lesson.getDate() != null
				&& !lesson.getGroups().isEmpty()
				&& lesson.getRoom() != null
				&& lesson.getTeacher() != null
				&& lesson.getTimeframe() != null
				&& isTeacherAvailable(lesson)
				&& isRoomAvailable(lesson)
				&& isGroupAvailable(lesson)
				&& isRoomCapacityCompatible(lesson)
				&& isTeacherCourseCompatible(lesson)
				&& isCourseRoomCompatible(lesson)
				&& !isAtWeekend(lesson);
	}

	private boolean isTeacherAvailable(Lesson lesson) {
		return lessonDao.getLessonsByTeacherAndDate(lesson.getTeacher(), lesson.getDate())
				.stream()
				.noneMatch(l -> l.getTimeframe().equals(lesson.getTimeframe()));
	}

	private boolean isRoomAvailable(Lesson lesson) {
		return lessonDao.getLessonsByRoomAndDate(lesson.getRoom(), lesson.getDate())
				.stream()
				.noneMatch(l -> l.getTimeframe().equals(lesson.getTimeframe()));
	}

	private boolean isGroupAvailable(Lesson lesson) {
		return lesson.getGroups()
				.stream()
				.map(g -> lessonDao.getLessonsByGroupIdAndDate(g.getId(), lesson.getDate()))
				.flatMap(Collection::stream)
				.noneMatch(l -> l.getTimeframe().equals(lesson.getTimeframe()));
	}

	private boolean isRoomCapacityCompatible(Lesson lesson) {
		return lesson.getGroups()
				.stream()
				.map(studentDao::getStudentsByGroup)
				.flatMap(Collection::stream)
				.count()
				<= lesson.getRoom().getCapacity();
	}

	public boolean isTeacherCourseCompatible(Lesson lesson) {
		return lesson.getTeacher()
				.getCourses()
				.contains(lesson.getCourse());
	}

	private boolean isCourseRoomCompatible(Lesson lesson) {
		return lesson.getCourse()
				.getRooms()
				.contains(lesson.getRoom());
	}

	private boolean isAtWeekend(Lesson lesson) {
		DayOfWeek day = lesson.getDate().getDayOfWeek();
		return day.equals(SATURDAY) || day.equals(SUNDAY);
	}

	private boolean isPresentById(Long id) {
		try {
			return Optional.of(lessonDao.findById(id)).isPresent();
		} catch (EmptyResultDataAccessException exeption) {
			return false;
		}
	}
}
