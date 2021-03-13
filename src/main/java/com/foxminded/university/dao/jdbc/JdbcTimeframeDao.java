package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.TimeframeMapper.TIMEFRAME_ID;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.dao.exception.DaoException;
import com.foxminded.university.dao.jdbc.mapper.TimeframeMapper;
import com.foxminded.university.model.Timeframe;

@Component
public class JdbcTimeframeDao implements TimeframeDao {

	private static final String CREATE_TIMEFRAME_QUERY = "INSERT INTO timeframes (sequence, start_time, end_time) "
			+ "VALUES (?, ?, ?)";
	private static final String FIND_TIMEFRAME_BY_ID_QUERY = "SELECT * FROM timeframes WHERE id = ?";
	private static final String GET_TIMEFRAMES_QUERY = "SELECT * FROM timeframes";
	private static final String DELETE_TIMEFRAME_BY_ID_QUERY = "DELETE FROM timeframes WHERE id = ?";
	private static final String UPDATE_TIMEFRAME_QUERY = "UPDATE timeframes SET sequence = ?, start_time = ?, end_time = ? WHERE id = ?";
	private static final String FIND_TIMEFRAME_BY_SEQUENCE_QUERY = "SELECT * FROM timeframes WHERE sequence = ?";
	private static final String COUNT_TIMEFRAMES_QUERY = "SELECT count(*) FROM timeframes";
	private static final String GET_TIMEFRAMES_ORDERED_BY_SEQUENCE_WITH_LIMIT_AND_OFFSET_QUERY = "SELECT * FROM timeframes ORDER BY sequence LIMIT ? OFFSET ?";

	private JdbcTemplate jdbcTemplate;
	private TimeframeMapper timeframeMapper;

	public JdbcTimeframeDao(JdbcTemplate jdbcTemplate, TimeframeMapper timeframeMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.timeframeMapper = timeframeMapper;
	}

	@Override
	public void create(Timeframe timeframe) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement statement = connection.prepareStatement(CREATE_TIMEFRAME_QUERY,
						new String[] { TIMEFRAME_ID });
				statement.setInt(1, timeframe.getSequence());
				statement.setObject(2, timeframe.getStartTime());
				statement.setObject(3, timeframe.getEndTime());
				return statement;
			}, keyHolder);
			timeframe.setId(keyHolder.getKey().longValue());
		} catch (DataAccessException e) {
			throw new DaoException("Could not create timeframe: " + timeframe, e);
		}
	}

	@Override
	public Optional<Timeframe> findById(Long timeframeId) {
		try {
			return Optional.of(jdbcTemplate
					.queryForObject(FIND_TIMEFRAME_BY_ID_QUERY, new Object[] { timeframeId }, timeframeMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DaoException("Could not get timeframe by id: " + timeframeId, e);
		}
	}

	@Override
	public List<Timeframe> getAll() {
		try {
			return jdbcTemplate.query(GET_TIMEFRAMES_QUERY, timeframeMapper);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get timeframes", e);
		}
	}

	@Override
	public void update(Timeframe timeframe) {
		try {
			jdbcTemplate.update(UPDATE_TIMEFRAME_QUERY,
					timeframe.getSequence(),
					timeframe.getStartTime(),
					timeframe.getEndTime(),
					timeframe.getId());
		} catch (DataAccessException e) {
			throw new DaoException("Could not update timeframe: " + timeframe, e);
		}
	}

	@Override
	public void deleteById(Long timeframeId) {
		try {
			jdbcTemplate.update(DELETE_TIMEFRAME_BY_ID_QUERY, timeframeId);
		} catch (DataAccessException e) {
			throw new DaoException("Could not delete timeframe by id: " + timeframeId, e);
		}
	}

	@Override
	public Optional<Timeframe> findBySequence(int sequence) {
		try {
			return Optional.of(jdbcTemplate
					.queryForObject(FIND_TIMEFRAME_BY_SEQUENCE_QUERY, new Object[] { sequence }, timeframeMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		} catch (DataAccessException e) {
			throw new DaoException("Could not get timeframe by sequence: " + sequence, e);
		}
	}

	@Override
	public int count() {
		try {
			return jdbcTemplate.queryForObject(COUNT_TIMEFRAMES_QUERY, Integer.class);
		} catch (DataAccessException e) {
			throw new DaoException("Could not get amount of timeframes", e);
		}
	}

	@Override
	public Page<Timeframe> getAllPage(Pageable pageable) {
		try {
			List<Timeframe> timeframes = jdbcTemplate.query(
					GET_TIMEFRAMES_ORDERED_BY_SEQUENCE_WITH_LIMIT_AND_OFFSET_QUERY,
					new Object[] { pageable.getPageSize(), pageable.getOffset() },
					timeframeMapper);
			return new PageImpl<>(timeframes, pageable, count());
		} catch (DataAccessException e) {
			throw new DaoException("Could not get timeframes", e);
		}
	}
}
