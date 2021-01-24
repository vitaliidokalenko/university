package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
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
		if (isTimeframeValid(timeframe)) {
			timeframeDao.create(timeframe);
		}
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
		if (isTimeframeValid(timeframe)) {
			timeframeDao.update(timeframe);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			timeframeDao.deleteById(id);
		}
	}

	private boolean isTimeframeValid(Timeframe timeframe) {
		return timeframe.getSequance() > 0
				&& timeframe.getStartTime() != null
				&& timeframe.getEndTime() != null
				&& timeframe.getStartTime().isBefore(timeframe.getEndTime());
	}

	private boolean isPresentById(Long id) {
		try {
			return Optional.of(timeframeDao.findById(id)).isPresent();
		} catch (EmptyResultDataAccessException exeption) {
			return false;
		}
	}
}
