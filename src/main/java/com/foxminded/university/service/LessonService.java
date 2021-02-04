package com.foxminded.university.service;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.exception.DAOException;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.service.exception.ServiceException;

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
			try {
				lessonDao.create(lesson);
			} catch (DAOException e) {
				throw new ServiceException("Could not create lesson: " + lesson, e);
			}
		}
	}

	@Transactional
	public Optional<Lesson> findById(Long id) {
		try {
			return lessonDao.findById(id);
		} catch (DAOException e) {
			throw new ServiceException("Could not get lesson by id: " + id, e);
		}
	}

	@Transactional
	public List<Lesson> getAll() {
		try {
			return lessonDao.getAll();
		} catch (DAOException e) {
			throw new ServiceException("Could not get lessons", e);
		}
	}

	@Transactional
	public void update(Lesson lesson) {
		if (isLessonValid(lesson)) {
			try {
				lessonDao.update(lesson);
			} catch (DAOException e) {
				throw new ServiceException("Could not update lesson: " + lesson, e);
			}
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			try {
				lessonDao.deleteById(id);
			} catch (DAOException e) {
				throw new ServiceException("Could not delete lesson by id: " + id, e);
			}
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
		Optional<Lesson> lessonByCriteria = lessonDao
				.getByTeacherAndDateAndTimeframe(lesson.getTeacher(), lesson.getDate(), lesson.getTimeframe());
		if (lessonByCriteria.isPresent()) {
			return lessonByCriteria.get().getId().equals(lesson.getId());
		} else {
			return true;
		}
	}

	private boolean isRoomAvailable(Lesson lesson) {
		Optional<Lesson> lessonByCriteria = lessonDao
				.getByRoomAndDateAndTimeframe(lesson.getRoom(), lesson.getDate(), lesson.getTimeframe());
		if (lessonByCriteria.isPresent()) {
			return lessonByCriteria.get().getId().equals(lesson.getId());
		} else {
			return true;
		}
	}

	private boolean isGroupAvailable(Lesson lesson) {
		return lesson.getGroups()
				.stream()
				.map(g -> lessonDao.getByGroupIdAndDateAndTimeframe(g.getId(), lesson.getDate(), lesson.getTimeframe()))
				.filter(Optional::isPresent)
				.allMatch(l -> l.get().getId().equals(lesson.getId()));
	}

	private boolean isRoomCapacityCompatible(Lesson lesson) {
		return lesson.getGroups()
				.stream()
				.map(studentDao::getByGroup)
				.mapToInt(Collection::size)
				.sum()
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
		return lessonDao.findById(id).isPresent();
	}
}
