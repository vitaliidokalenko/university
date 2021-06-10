package com.foxminded.university.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Group;

public interface GroupDao extends JpaRepository<Group, Long> {

	Optional<Group> findByName(String name);
}
