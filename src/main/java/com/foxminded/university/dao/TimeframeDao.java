package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import com.foxminded.university.model.Timeframe;

public interface TimeframeDao extends GenericDao<Timeframe> {

	public List<Timeframe> getAll();

	public Optional<Timeframe> findBySequence(int sequence);
}
