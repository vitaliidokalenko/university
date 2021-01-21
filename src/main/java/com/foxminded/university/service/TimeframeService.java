package com.foxminded.university.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Timeframe;

@Service
public class TimeframeService {

	private TimeframeDao timeframeDao;

	public TimeframeService(TimeframeDao timeframeDao) {
		this.timeframeDao = timeframeDao;
	}

	@Transactional
	public void create(Timeframe timeframe) {
		timeframeDao.create(timeframe);
	}

	@Transactional
	public Timeframe findById(Long id) {
		return timeframeDao.findById(id);
	}

	@Transactional
	public List<Timeframe> getAll() {
		return timeframeDao.getAll();
	}

	@Transactional
	public void update(Timeframe timeframe) {
		timeframeDao.update(timeframe);
	}

	@Transactional
	public void deleteById(Long id) {
		timeframeDao.deleteById(id);
	}
}
