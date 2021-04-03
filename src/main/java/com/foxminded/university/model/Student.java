package com.foxminded.university.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

	private Long id;
	private String name;
	private String surname;
	private Group group;
	private Set<Course> courses = new HashSet<>();
	private String phone;
	private String email;
	private String address;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate birthDate;
	private Gender gender;

	public Student(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}
}
