package com.foxminded.university.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Teacher;

public interface TeacherDao extends JpaRepository<Teacher, Long> {

	public List<Teacher> getByCoursesId(Long id);
}
