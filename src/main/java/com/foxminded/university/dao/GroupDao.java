package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.foxminded.university.model.Group;

public interface GroupDao extends JpaRepository<Group, Long> {

	@Query("select g from Group g where g.id in (select g.id from Lesson l join l.groups g where l.id = :id)")
	public List<Group> getByLessonId(Long id);

	public Optional<Group> findByName(String name);
}
