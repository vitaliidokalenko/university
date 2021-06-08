package com.foxminded.university.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foxminded.university.model.Teacher;

public interface TeacherDao extends JpaRepository<Teacher, Long> {

	@Query("select t from Teacher t join t.courses c where c.id = :id")
	public List<Teacher> getByCourseId(Long id);
}
