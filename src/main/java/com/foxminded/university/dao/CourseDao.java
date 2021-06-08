package com.foxminded.university.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Course;

public interface CourseDao extends JpaRepository<Course, Long> {

	public Optional<Course> findByName(String name);
}