package com.foxminded.university.service;

import static java.lang.String.format;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.service.exception.IncompatibleDateException;
import com.foxminded.university.service.exception.IncompatibleRoomException;
import com.foxminded.university.service.exception.IncompleteEntityException;
import com.foxminded.university.service.exception.LackOfCapacityException;
import com.foxminded.university.service.exception.NotAvailableGroupException;
import com.foxminded.university.service.exception.NotAvailableRoomException;
import com.foxminded.university.service.exception.NotAvailableTeacherException;
import com.foxminded.university.service.exception.NotCompetentTeacherException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Service
public class LessonService {

	private static final Logger logger = LoggerFactory.getLogger(LessonService.class);

	private LessonDao lessonDao;
	private StudentDao studentDao;

	public LessonService(LessonDao lessonDao, StudentDao studentDao) {
		this.lessonDao = lessonDao;
		this.studentDao = studentDao;
	}

	@Transactional
	public void create(Lesson lesson) {
		logger.debug("Creating lesson: {}", lesson);
		verify(lesson);
		lessonDao.create(lesson);
	}

	@Transactional
	public Optional<Lesson> findById(Long id) {
		logger.debug("Finding lesson by id: {}", id);
		return lessonDao.findById(id);
	}

	@Transactional
	public List<Lesson> getAll() {
		logger.debug("Getting lessons");
		return lessonDao.getAll();
	}

	@Transactional
	public void update(Lesson lesson) {
		logger.debug("Updating lesson: {}", lesson);
		verify(lesson);
		lessonDao.update(lesson);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting lesson by id: {}", id);
		if (lessonDao.findById(id).isPresent()) {
			lessonDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(format("Cannot find lesson by id: %d", id));
		}
	}

	private void verify(Lesson lesson) {
		verifyFields(lesson);
		verifyTeacherIsAvailable(lesson);
		verifyRoomIsAvailable(lesson);
		verifyGroups(lesson);
		verifyRoomCapacityIsEnough(lesson);
		verifyTeacherIsCompetentInCourse(lesson);
		verifyRoomIsAssignedForLessonCourse(lesson);
		verifyNotAtWeekend(lesson);
	}

	private void verifyFields(Lesson lesson) {
		if (lesson.getCourse() == null) {
			throw new IncompleteEntityException("There is not any course assigned to the lesson");
		} else if (lesson.getDate() == null) {
			throw new IncompleteEntityException("There is not any date assigned to the lesson");
		} else if (lesson.getGroups().isEmpty()) {
			throw new IncompleteEntityException("There is not any group assigned to the lesson");
		} else if (lesson.getRoom() == null) {
			throw new IncompleteEntityException("There is not any room assigned to the lesson");
		} else if (lesson.getTeacher() == null) {
			throw new IncompleteEntityException("There is not any teacher assigned to the lesson");
		} else if (lesson.getTimeframe() == null) {
			throw new IncompleteEntityException("There is not any timeframe assigned to the lesson");
		}
	}

	private void verifyTeacherIsAvailable(Lesson lesson) {
		Optional<Lesson> lessonByCriteria = lessonDao
				.getByTeacherAndDateAndTimeframe(lesson.getTeacher(), lesson.getDate(), lesson.getTimeframe());
		if (lessonByCriteria.isPresent() && !lessonByCriteria.get().getId().equals(lesson.getId())) {
			throw new NotAvailableTeacherException(format("Teacher %s %s is busy at the time %s, %s",
					lesson.getTeacher().getName(),
					lesson.getTeacher().getSurname(),
					lesson.getTimeframe().getStartTime().toString(),
					lesson.getDate().toString()));
		}
	}

	private void verifyRoomIsAvailable(Lesson lesson) {
		Optional<Lesson> lessonByCriteria = lessonDao
				.getByRoomAndDateAndTimeframe(lesson.getRoom(), lesson.getDate(), lesson.getTimeframe());
		if (lessonByCriteria.isPresent() && !lessonByCriteria.get().getId().equals(lesson.getId())) {
			throw new NotAvailableRoomException(format("Room %s is occupied at the time %s, %s",
					lesson.getRoom().getName(),
					lesson.getTimeframe().getStartTime().toString(),
					lesson.getDate().toString()));
		}
	}

	private void verifyGroups(Lesson lesson) {
		lesson.getGroups().forEach(g -> verifyGroupIsAvailable(g, lesson));
	}

	private void verifyGroupIsAvailable(Group group, Lesson lesson) {
		Optional<Lesson> lessonByCriteria = lessonDao
				.getByGroupIdAndDateAndTimeframe(group.getId(), lesson.getDate(), lesson.getTimeframe());
		if (lessonByCriteria.isPresent() && !lessonByCriteria.get().getId().equals(lesson.getId())) {
			throw new NotAvailableGroupException(
					format("Other lesson was scheduled for the group %s at the time %s, %s",
							group.getName(),
							lesson.getTimeframe().getStartTime().toString(),
							lesson.getDate().toString()));
		}
	}

	private void verifyRoomCapacityIsEnough(Lesson lesson) {
		int count = lesson.getGroups()
				.stream()
				.map(studentDao::getByGroup)
				.mapToInt(Collection::size)
				.sum();
		if (count > lesson.getRoom().getCapacity()) {
			throw new LackOfCapacityException(
					format("Capacity of the room %s (%d seats) is not enough for %d students",
							lesson.getRoom().getName(),
							lesson.getRoom().getCapacity(),
							count));
		}
	}

	private void verifyTeacherIsCompetentInCourse(Lesson lesson) {
		if (!lesson.getTeacher().getCourses().contains(lesson.getCourse())) {
			throw new NotCompetentTeacherException(
					format("The teacher %s %s is incompetent to lecture course %s for the lesson",
							lesson.getTeacher().getName(),
							lesson.getTeacher().getSurname(),
							lesson.getCourse()));
		}
	}

	private void verifyRoomIsAssignedForLessonCourse(Lesson lesson) {
		if (!lesson.getCourse().getRooms().contains(lesson.getRoom())) {
			throw new IncompatibleRoomException(format("Course %s cannot be lectured in the room %s",
					lesson.getCourse().getName(),
					lesson.getRoom().getName()));
		}
	}

	private void verifyNotAtWeekend(Lesson lesson) {
		DayOfWeek day = lesson.getDate().getDayOfWeek();
		if (day.equals(SATURDAY) || day.equals(SUNDAY)) {
			throw new IncompatibleDateException(
					format("The date the lesson appointed at is at the weekend (%s)", lesson.getDate().toString()));
		}
	}
}
