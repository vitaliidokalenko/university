package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Timeframe;

public interface TimeframeDao {

	public void create(Timeframe timeframe);

	public Timeframe findById(Long timeframeId);

	public List<Timeframe> getAll();

	public void update(Timeframe timeframe);

	public void deleteById(Long timeframeId);
}
