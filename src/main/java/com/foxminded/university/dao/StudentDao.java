package com.foxminded.university.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

public interface StudentDao extends JpaRepository<Student, Long> {

	public List<Student> getByGroup(Group group);

	@Query("select s from Student s join s.courses c where c.id = :id")
	public List<Student> getByCourseId(Long id);
}
