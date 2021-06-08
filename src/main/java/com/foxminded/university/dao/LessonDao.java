package com.foxminded.university.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

public interface LessonDao extends JpaRepository<Lesson, Long> {

	@Query("select l from Lesson l join l.groups g where g.id = :id and l.date = :date and l.timeframe = :timeframe")
	public Optional<Lesson> getByGroupIdAndDateAndTimeframe(Long id, LocalDate date, Timeframe timeframe);

	public Optional<Lesson> getByTeacherAndDateAndTimeframe(Teacher teacher, LocalDate date, Timeframe timeframe);

	public Optional<Lesson> getByRoomAndDateAndTimeframe(Room room, LocalDate date, Timeframe timeframe);

	public List<Lesson> getByTeacherIdAndDateBetween(Long id, LocalDate startDate, LocalDate endDate);

	@Query("select l from Lesson l join l.groups g where g.id = :id and l.date between :startDate and :endDate")
	public List<Lesson> getByGroupIdAndDateBetween(Long id, LocalDate startDate, LocalDate endDate);
}
