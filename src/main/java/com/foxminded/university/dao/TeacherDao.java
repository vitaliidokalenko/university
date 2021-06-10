package com.foxminded.university.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Course;
import com.foxminded.university.model.Teacher;

public interface TeacherDao extends JpaRepository<Teacher, Long> {

	List<Teacher> getByCourses(Course course);
}
