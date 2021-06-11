package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

public interface LessonDao extends JpaRepository<Lesson, Long> {

	Optional<Lesson> getByGroupsAndDateAndTimeframe(Group group, LocalDate date, Timeframe timeframe);

	Optional<Lesson> getByTeacherAndDateAndTimeframe(Teacher teacher, LocalDate date, Timeframe timeframe);

	Optional<Lesson> getByRoomAndDateAndTimeframe(Room room, LocalDate date, Timeframe timeframe);

	List<Lesson> getByTeacherAndDateBetween(Teacher teacher, LocalDate startDate, LocalDate endDate);

	List<Lesson> getByGroupsAndDateBetween(Group group, LocalDate startDate, LocalDate endDate);
}
