package com.foxminded.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.dao.exception.DAOException;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.exception.ServiceException;

@Service
public class TimeframeService {

	private TimeframeDao timeframeDao;

	public TimeframeService(TimeframeDao timeframeDao) {
		this.timeframeDao = timeframeDao;
	}

	@Transactional
	public void create(Timeframe timeframe) {
		if (isTimeframeValid(timeframe)) {
			try {
				timeframeDao.create(timeframe);
			} catch (DAOException e) {
				throw new ServiceException("Could not create timeframe: " + timeframe, e);
			}
		}
	}

	@Transactional
	public Optional<Timeframe> findById(Long id) {
		try {
			return timeframeDao.findById(id);
		} catch (DAOException e) {
			throw new ServiceException("Could not get timeframe by id: " + id, e);
		}
	}

	@Transactional
	public List<Timeframe> getAll() {
		try {
			return timeframeDao.getAll();
		} catch (DAOException e) {
			throw new ServiceException("Could not get timeframes", e);
		}
	}

	@Transactional
	public void update(Timeframe timeframe) {
		if (isTimeframeValid(timeframe)) {
			try {
				timeframeDao.update(timeframe);
			} catch (DAOException e) {
				throw new ServiceException("Could not update timeframe: " + timeframe, e);
			}
		}
	}

	@Transactional
	public void deleteById(Long id) {
		if (isPresentById(id)) {
			try {
				timeframeDao.deleteById(id);
			} catch (DAOException e) {
				throw new ServiceException("Could not delete timeframe by id: " + id, e);
			}
		}
	}

	private boolean isTimeframeValid(Timeframe timeframe) {
		return timeframe.getSequance() > 0
				&& timeframe.getStartTime() != null
				&& timeframe.getEndTime() != null
				&& timeframe.getStartTime().isBefore(timeframe.getEndTime());
	}

	private boolean isPresentById(Long id) {
		return timeframeDao.findById(id).isPresent();
	}
}
