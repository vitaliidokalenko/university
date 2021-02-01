package com.foxminded.university.dao.jdbc;

import static com.foxminded.university.dao.jdbc.mapper.TimeframeMapper.TIMEFRAME_ID;
import static java.sql.Types.INTEGER;
import static java.sql.Types.TIME;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.dao.jdbc.mapper.TimeframeMapper;
import com.foxminded.university.model.Timeframe;

@Component
public class JdbcTimeframeDao implements TimeframeDao {

	private static final String CREATE_TIMEFRAME_QUERY = "INSERT INTO timeframes (sequance, start_time, end_time) "
			+ "VALUES (?, ?, ?)";
	private static final String FIND_TIMEFRAME_BY_ID_QUERY = "SELECT * FROM timeframes WHERE id = ?";
	private static final String GET_TIMEFRAMES_QUERY = "SELECT * FROM timeframes";
	private static final String DELETE_TIMEFRAME_BY_ID_QUERY = "DELETE FROM timeframes WHERE id = ?";
	private static final String UPDATE_TIMEFRAME_QUERY = "UPDATE timeframes SET sequance = ?, start_time = ?, end_time = ? WHERE id = ?";

	private JdbcTemplate jdbcTemplate;
	private TimeframeMapper timeframeMapper;

	public JdbcTimeframeDao(JdbcTemplate jdbcTemplate, TimeframeMapper timeframeMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.timeframeMapper = timeframeMapper;
	}

	@Override
	public void create(Timeframe timeframe) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement statement = connection.prepareStatement(CREATE_TIMEFRAME_QUERY,
					new String[] { TIMEFRAME_ID });
			statement.setInt(1, timeframe.getSequance());
			statement.setObject(2, timeframe.getStartTime());
			statement.setObject(3, timeframe.getEndTime());
			return statement;
		}, keyHolder);
		timeframe.setId(keyHolder.getKey().longValue());
	}

	@Override
	public Optional<Timeframe> findById(Long timeframeId) {
		try {
			return Optional.of(jdbcTemplate
					.queryForObject(FIND_TIMEFRAME_BY_ID_QUERY, new Object[] { timeframeId }, timeframeMapper));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public List<Timeframe> getAll() {
		return jdbcTemplate.query(GET_TIMEFRAMES_QUERY, timeframeMapper);
	}

	@Override
	public void update(Timeframe timeframe) {
		jdbcTemplate.update(UPDATE_TIMEFRAME_QUERY,
				new Object[] { timeframe.getSequance(), timeframe.getStartTime(), timeframe.getEndTime(),
						timeframe.getId() },
				new int[] { INTEGER, TIME, TIME, INTEGER });
	}

	@Override
	public void deleteById(Long timeframeId) {
		jdbcTemplate.update(DELETE_TIMEFRAME_BY_ID_QUERY, timeframeId);
	}
}
