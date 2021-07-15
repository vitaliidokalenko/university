package com.foxminded.university.service;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
import com.foxminded.university.service.exception.NotAvailableGroupException;
import com.foxminded.university.service.exception.NotAvailableRoomException;
import com.foxminded.university.service.exception.NotAvailableTeacherException;
import com.foxminded.university.service.exception.NotCompetentTeacherForCourseException;
import com.foxminded.university.service.exception.NotEnoughRoomCapacityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotFoundSubstituteTeacherException;
import com.foxminded.university.service.exception.NotSuitableRoomForCourseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LessonService {

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
		log.debug("Creating lesson: {}", lesson);
		verify(lesson);
		lessonDao.save(lesson);
	}

	@Transactional
	public Lesson findById(Long id) {
		log.debug("Finding lesson by id: {}", id);
		return lessonDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find lesson by id: %d", id)));
	}

	@Transactional
	public Page<Lesson> getAllPage(Pageable pageable) {
		log.debug("Getting pageable lessons");
		return lessonDao.findAll(pageable);
	}

	@Transactional
	public void update(Lesson lesson) {
		log.debug("Updating lesson: {}", lesson);
		if (findById(lesson.getId()) != null) {
			verify(lesson);
			lessonDao.save(lesson);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		log.debug("Deleting lesson by id: {}", id);
		lessonDao.delete(findById(id));
	}

	@Transactional
	public List<Lesson> getByTeacherAndDateBetween(Teacher teacher, LocalDate startDate, LocalDate endDate) {
		log.debug("Getting lessons by teacher: {} and dates: between {} and {}", teacher, startDate, endDate);
		return lessonDao.getByTeacherAndDateBetween(teacher, startDate, endDate);
	}

	@Transactional
	public List<Lesson> getByGroupAndDateBetween(Group group, LocalDate startDate, LocalDate endDate) {
		log.debug("Getting lessons by group: {} and dates: between {} and {}", group, startDate, endDate);
		return lessonDao.getByGroupsAndDateBetween(group, startDate, endDate);
	}

	@Transactional
	public void replaceTeacherByDateBetween(Teacher teacher, LocalDate startDate, LocalDate endDate,
			List<Long> substituteTeacherIds) {
		log.debug("Replacing teacher: {} {} for lessons between {} and {}",
				teacher.getName(),
				teacher.getSurname(),
				startDate,
				endDate);
		List<Lesson> lessons = lessonDao.getByTeacherAndDateBetween(teacher, startDate, endDate);
		if (substituteTeacherIds == null) {
			lessons.forEach(l -> replaceTeacher(l, teacherDao.getByCourses(l.getCourse())));
		} else {
			List<Teacher> substituteTeachers = substituteTeacherIds.stream()
					.map(id -> teacherDao.findById(id)
							.orElseThrow(() -> new NotFoundEntityException(
									format("Cannot find teacher by id: %d", id))))
					.collect(toList());
			lessons.forEach(l -> replaceTeacher(l, substituteTeachers));
		}
		lessons.stream().forEach(lessonDao::save);
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
		verifyTeacherIsAvailable(lesson);
		verifyRoomIsAvailable(lesson);
		verifyGroups(lesson);
		verifyRoomCapacityIsEnough(lesson);
		verifyTeacherIsCompetentInCourse(lesson);
		verifyRoomIsAssignedForLessonCourse(lesson);
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
				.getByGroupsAndDateAndTimeframe(group, lesson.getDate(), lesson.getTimeframe());
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
}
