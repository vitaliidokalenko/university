package com.foxminded.university.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

public interface StudentDao extends JpaRepository<Student, Long> {

	List<Student> getByGroup(Group group);
}
