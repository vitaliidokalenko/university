package com.foxminded.university.service;

import static java.lang.String.format;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.config.UniversityConfigProperties;
import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Timeframe;
import com.foxminded.university.service.exception.IncorrectDurationException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueSequenceException;

@Service
public class TimeframeService {

	private static final Logger logger = LoggerFactory.getLogger(TimeframeService.class);

	private TimeframeDao timeframeDao;
	private UniversityConfigProperties properties;

	public TimeframeService(TimeframeDao timeframeDao, UniversityConfigProperties properties) {
		this.timeframeDao = timeframeDao;
		this.properties = properties;
	}

	@Transactional
	public void create(Timeframe timeframe) {
		logger.debug("Creating timeframe: {}", timeframe);
		verify(timeframe);
		timeframeDao.save(timeframe);
	}

	@Transactional
	public Optional<Timeframe> findById(Long id) {
		logger.debug("Finding timeframe by id: {}", id);
		return timeframeDao.findById(id);
	}

	@Transactional
	public List<Timeframe> getAll() {
		logger.debug("Getting timeframes");
		return timeframeDao.findAll();
	}

	@Transactional
	public Page<Timeframe> getAllPage(Pageable pageable) {
		logger.debug("Getting pageable timeframes");
		return timeframeDao.findAll(pageable);
	}

	@Transactional
	public void update(Timeframe timeframe) {
		logger.debug("Updating timeframe: {}", timeframe);
		verify(timeframe);
		timeframeDao.save(timeframe);
	}

	@Transactional
	public void deleteById(Long id) {
		logger.debug("Deleting timeframe by id: {}", id);
		timeframeDao.delete(timeframeDao.findById(id)
				.orElseThrow(() -> new NotFoundEntityException(format("Cannot find timeframe by id: %d", id))));
	}

	private void verify(Timeframe timeframe) {
		verifyDuration(timeframe);
		verifySequenceIsUnique(timeframe);
	}

	private void verifyDuration(Timeframe timeframe) {
		if (!Duration.between(timeframe.getStartTime(), timeframe.getEndTime())
				.equals(properties.getLessonDuration())) {
			throw new IncorrectDurationException(
					format("Not valid timeframe duration. It must be %smin.",
							properties.getLessonDuration().toMinutes()));
		}
	}

	private void verifySequenceIsUnique(Timeframe timeframe) {
		Optional<Timeframe> timeframeBySequence = timeframeDao.findBySequence(timeframe.getSequence());
		if (timeframeBySequence.isPresent() && !timeframeBySequence.get().getId().equals(timeframe.getId())) {
			throw new NotUniqueSequenceException(
					format("The timeframe with sequence: %d already exists", timeframe.getSequence()));
		}
	}
}
