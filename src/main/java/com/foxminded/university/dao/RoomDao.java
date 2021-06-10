package com.foxminded.university.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Room;

public interface RoomDao extends JpaRepository<Room, Long> {

	Optional<Room> findByName(String name);
}
