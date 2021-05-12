package com.foxminded.university.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedQuery(
		name = "getAllCourses",
		query = "from Course c")
@NamedQuery(
		name = "countCourses",
		query = "select count(c) from Course c")
@NamedQuery(
		name = "getCoursesByRoomId",
		query = "select c from Course c join c.rooms r where r.id = :id")
@NamedQuery(
		name = "getCoursesByStudentId",
		query = "select c from Student s join s.courses c where s.id = :id")
@NamedQuery(
		name = "getCoursesByTeacherId",
		query = "select c from Teacher t join t.courses c where t.id = :id")
@NamedQuery(
		name = "findCourseByName",
		query = "from Course c where c.name = :name")
@Entity
@Table(name = "courses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	@ManyToMany
	@JoinTable(name = "courses_rooms",
			joinColumns = @JoinColumn(name = "course_id"),
			inverseJoinColumns = @JoinColumn(name = "room_id"))
	private Set<Room> rooms = new HashSet<>();

	public Course(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Course other = (Course) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + "]";
	}
}
