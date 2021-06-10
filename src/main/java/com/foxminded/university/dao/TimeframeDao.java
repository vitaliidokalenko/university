package com.foxminded.university.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foxminded.university.model.Timeframe;

public interface TimeframeDao extends JpaRepository<Timeframe, Long> {

	Optional<Timeframe> findBySequence(int sequence);
}
