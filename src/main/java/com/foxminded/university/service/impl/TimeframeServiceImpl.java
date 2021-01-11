package com.foxminded.university.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.TimeframeService;

@Service
public class TimeframeServiceImpl implements TimeframeService {

	private TimeframeDao timeframeDao;

	public TimeframeServiceImpl(TimeframeDao timeframeDao) {
		this.timeframeDao = timeframeDao;
	}

	@Override
	@Transactional
	public void create(Timeframe timeframe) {
		timeframeDao.create(timeframe);
	}

	@Override
	@Transactional
	public Timeframe findById(Long id) {
		return timeframeDao.findById(id);
	}

	@Override
	@Transactional
	public List<Timeframe> getAll() {
		return timeframeDao.getAll();
	}

	@Override
	@Transactional
	public void update(Timeframe timeframe) {
		timeframeDao.update(timeframe);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		timeframeDao.deleteById(id);
	}

	@Override
	@Transactional
	public boolean existsById(Long id) {
		return Optional.of(timeframeDao.findById(id)).isPresent();
	}
}
