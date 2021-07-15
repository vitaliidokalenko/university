package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foxminded.university.validator.annotation.Age;
import com.foxminded.university.validator.annotation.Phone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "students")
@Getter
@Setter
@EqualsAndHashCode(of = { "id", "name", "surname", "email" })
@ToString(of = { "id", "name", "surname" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 50)
	private String name;

	@NotBlank
	@Size(max = 50)
	private String surname;

	@ManyToOne
	@JsonIgnoreProperties("students")
	private Group group;

	@ManyToMany
	@JoinTable(name = "students_courses",
			joinColumns = @JoinColumn(name = "student_id"),
			inverseJoinColumns = @JoinColumn(name = "course_id"))
	@JsonIgnoreProperties("rooms")
	private Set<Course> courses = new HashSet<>();

	@Phone
	private String phone;

	@Email
	@Size(max = 50)
	private String email;

	@Size(max = 100)
	private String address;

	@Past
	@Age(min = 14)
	@Column(name = "birth_date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate birthDate;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Gender gender;
}
