package com.foxminded.university.service;

import static java.lang.String.format;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.exception.AlreadyExistsEntityException;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;

@Service
@PropertySource("application.properties")
public class TimeframeService {

	private static final Logger logger = LoggerFactory.getLogger(TimeframeService.class);

	private TimeframeDao timeframeDao;
	@Value("#{T(java.time.Duration).parse('${timeframe.duration}')}")
	private Duration duration;

	public TimeframeService(TimeframeDao timeframeDao) {
		this.timeframeDao = timeframeDao;
	}

	@Transactional
	public void create(Timeframe timeframe) {
		logger.debug("Creating timeframe: {}", timeframe);
		verify(timeframe);
		timeframeDao.create(timeframe);
	}

	@Transactional
	public Optional<Timeframe> findById(Long id) {
		logger.debug("Finding timeframe by id: {}", id);
		return timeframeDao.findById(id);
	}

	@Transactional
	public List<Timeframe> getAll() {
		logger.debug("Getting timeframes");
		return timeframeDao.getAll();
	}

	@Transactional
	public void update(Timeframe timeframe) {
		logger.debug("Updating timeframe: {}", timeframe);
		verify(timeframe);
		timeframeDao.update(timeframe);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting timeframe by id: {}", id);
		if (isPresentById(id)) {
			timeframeDao.deleteById(id);
		} else {
			throw new NotFoundEntityException(
					format("There is nothing to delete. Timeframe with id: %d is absent", id));
		}
	}

	private void verify(Timeframe timeframe) {
		if (timeframe.getSequence() < 1) {
			throw new IllegalFieldEntityException("Sequence of the timeframe is less than 1");
		} else if (timeframe.getStartTime() == null) {
			throw new IllegalFieldEntityException("Start time of the timeframe is absent");
		} else if (timeframe.getEndTime() == null) {
			throw new IllegalFieldEntityException("End time of the timeframe is absent");
		} else if (timeframe.getStartTime().isAfter(timeframe.getEndTime())) {
			throw new IllegalFieldEntityException("Start time of the timeframe is after end time");
		} else if (!isDurationValid(timeframe)) {
			throw new IllegalFieldEntityException(
					format("Duration of the timeframe is not valid. It must be %smin.", duration.toMinutes()));
		} else if (!isSequenceUnique(timeframe)) {
			throw new AlreadyExistsEntityException(
					format("The timeframe with sequence: %d already exists", timeframe.getSequence()));
		}
	}

	private boolean isPresentById(Long id) {
		return timeframeDao.findById(id).isPresent();
	}

	private boolean isDurationValid(Timeframe timeframe) {
		return Duration.between(timeframe.getStartTime(), timeframe.getEndTime()).equals(duration);
	}

	private boolean isSequenceUnique(Timeframe timeframe) {
		Optional<Timeframe> timeframeBySequence = timeframeDao.findBySequence(timeframe.getSequence());
		if (timeframeBySequence.isPresent()) {
			return timeframeBySequence.get().getId().equals(timeframe.getId());
		} else {
			return true;
		}
	}
}
