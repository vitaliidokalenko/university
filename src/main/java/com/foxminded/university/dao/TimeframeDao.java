package com.foxminded.university.dao;

import java.util.Optional;

import com.foxminded.university.model.Timeframe;

public interface TimeframeDao extends GenericDao<Timeframe> {

	Optional<Timeframe> findBySequence(int sequence);
}
