package com.foxminded.university.service;

import static java.lang.String.format;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.util.stream.Collectors.toList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.exception.IncompleteEntityException;
import com.foxminded.university.service.exception.NotAvailableGroupException;
import com.foxminded.university.service.exception.NotAvailableRoomException;
import com.foxminded.university.service.exception.NotAvailableTeacherException;
import com.foxminded.university.service.exception.NotCompetentTeacherForCourseException;
import com.foxminded.university.service.exception.NotEnoughRoomCapacityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotFoundSubstituteTeacherException;
import com.foxminded.university.service.exception.NotSuitableRoomForCourseException;
import com.foxminded.university.service.exception.NotWeekDayException;

@Service
public class LessonService {

	private static final Logger logger = LoggerFactory.getLogger(LessonService.class);

	private LessonDao lessonDao;
	private StudentDao studentDao;
	private TeacherDao teacherDao;

	public LessonService(LessonDao lessonDao, StudentDao studentDao, TeacherDao teacherDao) {
		this.lessonDao = lessonDao;
		this.studentDao = studentDao;
		this.teacherDao = teacherDao;
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
	public Page<Lesson> getAllPage(Pageable pageable) {
		logger.debug("Getting pageable lessons");
		return lessonDao.getAllPage(pageable);
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

	@Transactional
	public List<Lesson> getByTeacherIdAndDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate) {
		logger.debug("Getting lessons by teacher id: {} and dates: between {} and {}", teacherId, startDate, endDate);
		return lessonDao.getByTeacherIdAndDateBetween(teacherId, startDate, endDate);
	}

	@Transactional
	public List<Lesson> getByGroupIdAndDateBetween(Long groupId, LocalDate startDate, LocalDate endDate) {
		logger.debug("Getting lessons by group id: {} and dates: between {} and {}", groupId, startDate, endDate);
		return lessonDao.getByGroupIdAndDateBetween(groupId, startDate, endDate);
	}

	@Transactional
	public void replaceTeacherByDateBetween(Teacher teacher, LocalDate startDate, LocalDate endDate,
			List<Long> substituteTeacherIds) {
		logger.debug("Replacing teacher: {} {} for lessons between {} and {}",
				teacher.getName(),
				teacher.getSurname(),
				startDate,
				endDate);
		List<Lesson> lessons = lessonDao.getByTeacherIdAndDateBetween(teacher.getId(), startDate, endDate);
		if (substituteTeacherIds == null) {
			lessons.forEach(l -> replaceTeacher(l, teacherDao.getByCourseId(l.getCourse().getId())));
		} else {
			List<Teacher> substituteTeachers = substituteTeacherIds.stream()
					.map(id -> teacherDao.findById(id)
							.orElseThrow(() -> new NotFoundEntityException(
									format("Cannot find teacher by id: %d", id))))
					.collect(toList());
			lessons.forEach(l -> replaceTeacher(l, substituteTeachers));
		}
		lessons.stream().forEach(lessonDao::update);
	}

	private void replaceTeacher(Lesson lesson, List<Teacher> substituteTeachers) {
		lesson.setTeacher(substituteTeachers.stream()
				.filter(t -> !lessonDao.getByTeacherAndDateAndTimeframe(t, lesson.getDate(), lesson.getTimeframe())
						.isPresent()
						&& !t.getId().equals(lesson.getTeacher().getId())
						&& t.getCourses().contains(lesson.getCourse()))
				.findAny()
				.orElseThrow(() -> new NotFoundSubstituteTeacherException(
						format("Substitute teacher was not found for the lesson id: %d, course: %s, date: %s, start time: %s",
								lesson.getId(),
								lesson.getCourse().getName(),
								lesson.getDate(),
								lesson.getTimeframe().getStartTime()))));
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
			throw new IncompleteEntityException("No course assigned to the lesson");
		} else if (lesson.getDate() == null) {
			throw new IncompleteEntityException("No date assigned to the lesson");
		} else if (lesson.getGroups().isEmpty()) {
			throw new IncompleteEntityException("No groups assigned to the lesson");
		} else if (lesson.getRoom() == null) {
			throw new IncompleteEntityException("No room assigned to the lesson");
		} else if (lesson.getTeacher() == null) {
			throw new IncompleteEntityException("No teacher assigned to the lesson");
		} else if (lesson.getTimeframe() == null) {
			throw new IncompleteEntityException("No timeframe assigned to the lesson");
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
			throw new NotEnoughRoomCapacityException(
					format("Capacity of the room %s (%d seats) is not enough for %d students",
							lesson.getRoom().getName(),
							lesson.getRoom().getCapacity(),
							count));
		}
	}

	private void verifyTeacherIsCompetentInCourse(Lesson lesson) {
		if (!lesson.getTeacher().getCourses().contains(lesson.getCourse())) {
			throw new NotCompetentTeacherForCourseException(
					format("The teacher %s %s is incompetent to lecture course %s",
							lesson.getTeacher().getName(),
							lesson.getTeacher().getSurname(),
							lesson.getCourse()));
		}
	}

	private void verifyRoomIsAssignedForLessonCourse(Lesson lesson) {
		if (!lesson.getCourse().getRooms().contains(lesson.getRoom())) {
			throw new NotSuitableRoomForCourseException(format("Course %s cannot be lectured in the room %s",
					lesson.getCourse().getName(),
					lesson.getRoom().getName()));
		}
	}

	private void verifyNotAtWeekend(Lesson lesson) {
		DayOfWeek day = lesson.getDate().getDayOfWeek();
		if (day == SATURDAY || day == SUNDAY) {
			throw new NotWeekDayException(
					format("Lesson cannot be appointed at the weekend (%s)", lesson.getDate().toString()));
		}
	}
}
