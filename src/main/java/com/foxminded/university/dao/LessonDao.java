package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

public interface LessonDao extends JpaRepository<Lesson, Long> {

	public Optional<Lesson> getByGroupsIdAndDateAndTimeframe(Long id, LocalDate date, Timeframe timeframe);

	public Optional<Lesson> getByTeacherAndDateAndTimeframe(Teacher teacher, LocalDate date, Timeframe timeframe);

	public Optional<Lesson> getByRoomAndDateAndTimeframe(Room room, LocalDate date, Timeframe timeframe);

	public List<Lesson> getByTeacherIdAndDateBetween(Long id, LocalDate startDate, LocalDate endDate);

	public List<Lesson> getByGroupsIdAndDateBetween(Long id, LocalDate startDate, LocalDate endDate);
}
