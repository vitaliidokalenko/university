package com.foxminded.university.service;

import static java.lang.String.format;
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
import com.foxminded.university.model.Lesson;
import com.foxminded.university.service.exception.IncompatibleDateException;
import com.foxminded.university.service.exception.IncompatibleRelationEntityException;
import com.foxminded.university.service.exception.IncompleteEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

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
		verify(lesson);
		lessonDao.create(lesson);
	}

	@Transactional
	public Optional<Lesson> findById(Long id) {
		return lessonDao.findById(id);
	}

	@Transactional
	public List<Lesson> getAll() {
		return lessonDao.getAll();
	}

	@Transactional
	public void update(Lesson lesson) {
		verify(lesson);
		lessonDao.update(lesson);
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			lessonDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(format("There is nothing to delete. Lesson with id: %d is absent", id));
		}
	}

	private void verify(Lesson lesson) {
		if (lesson.getCourse() == null) {
			throw new IncompleteEntityException("There is any course assigned to the lesson");
		} else if (lesson.getDate() == null) {
			throw new IncompleteEntityException("There is any date assigned to the lesson");
		} else if (lesson.getGroups().isEmpty()) {
			throw new IncompleteEntityException("There is any group assigned to the lesson");
		} else if (lesson.getRoom() == null) {
			throw new IncompleteEntityException("There is any room assigned to the lesson");
		} else if (lesson.getTeacher() == null) {
			throw new IncompleteEntityException("There is any teacher assigned to the lesson");
		} else if (lesson.getTimeframe() == null) {
			throw new IncompleteEntityException("There is any timeframe assigned to the lesson");
		} else if (!isTeacherAvailable(lesson)) {
			throw new IncompatibleRelationEntityException(format(
					"Teacher %s %s is not available for the lesson",
					lesson.getTeacher().getName(),
					lesson.getTeacher().getSurname()));
		} else if (!isRoomAvailable(lesson)) {
			throw new IncompatibleRelationEntityException(
					format("Room %s is not available for the lesson", lesson.getRoom().getName()));
		} else if (!isGroupAvailable(lesson)) {
			throw new IncompatibleRelationEntityException("Groups is not available for the lesson");
		} else if (!isRoomCapacityCompatible(lesson)) {
			throw new IncompatibleRelationEntityException(
					format("Capacity of the room %s is incompatible for the lesson. Size = %d seats",
							lesson.getRoom().getName(),
							lesson.getRoom().getCapacity()));
		} else if (!isTeacherCourseCompatible(lesson)) {
			throw new IncompatibleRelationEntityException(
					format("The teacher %s %s is incompetent to lecture course %s for the lesson",
							lesson.getTeacher().getName(),
							lesson.getTeacher().getSurname(),
							lesson.getCourse()));
		} else if (!isCourseRoomCompatible(lesson)) {
			throw new IncompatibleRelationEntityException(format("Course %s can not be lectured in the room %s",
					lesson.getCourse().getName(),
					lesson.getRoom().getName()));
		} else if (isAtWeekend(lesson)) {
			throw new IncompatibleDateException("The date the lesson appointed at is at the weekend");
		}
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
