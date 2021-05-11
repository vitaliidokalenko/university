package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedEntityGraph(
		name = "Lesson.groups",
		attributeNodes = { @NamedAttributeNode("groups") })
@NamedQuery(
		name = "getLessonById",
		query = "from Lesson l left join fetch l.groups where l.id = :id")
@NamedQuery(
		name = "getAllLessons",
		query = "from Lesson l")
@NamedQuery(
		name = "countLessons",
		query = "select count(l) from Lesson l")
@NamedQuery(
		name = "getLessonByGroupIdAndDateAndTimeframe",
		query = "select l from Lesson l join l.groups g where g.id = :id and l.date = :date and l.timeframe = :timeframe")
@NamedQuery(
		name = "getLessonsByTimeframe",
		query = "select l from Lesson l where l.timeframe = :timeframe")
@NamedQuery(
		name = "getLessonsByCourse",
		query = "select l from Lesson l where l.course = :course")
@NamedQuery(
		name = "getLessonByTeacherAndDateAndTimeframe",
		query = "select l from Lesson l where l.teacher = :teacher and l.date = :date and l.timeframe = :timeframe")
@NamedQuery(
		name = "getLessonByRoomAndDateAndTimeframe",
		query = "select l from Lesson l where l.room = :room and l.date = :date and l.timeframe = :timeframe")
@NamedQuery(
		name = "getLessonsByTeacherIdAndDateBetween",
		query = "select l from Lesson l where l.teacher.id = :id and l.date between :startDate and :endDate")
@NamedQuery(
		name = "getLessonsByGroupIdAndDateBetween",
		query = "select l from Lesson l join l.groups g where g.id = :id and l.date between :startDate and :endDate")
@Entity
@Table(name = "lessons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToMany
	@JoinTable(name = "lessons_groups",
			joinColumns = @JoinColumn(name = "lesson_id"),
			inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<Group> groups = new HashSet<>();
	@ManyToOne
	private Teacher teacher;
	@ManyToOne
	private Course course;
	@ManyToOne
	private Room room;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;
	@ManyToOne
	private Timeframe timeframe;
}
