package com.foxminded.university.dao.jdbc;

import static java.sql.Types.INTEGER;
import static java.sql.Types.TIME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.dao.jdbc.mapper.TimeframeMapper;
import com.foxminded.university.model.Timeframe;

@Component
public class JdbcTimeframeDao implements TimeframeDao {

	private static final String TIMEFRAMES_TABLE_NAME = "timeframes";
	private static final String TIMEFRAME_ID = "timeframe_id";
	private static final String TIMEFRAME_SEQUANCE = "timeframe_sequance";
	private static final String TIMEFRAME_START_TIME = "start_time";
	private static final String TIMEFRAME_END_TIME = "end_time";
	private static final String FIND_TIMEFRAME_BY_ID_QUERY = "SELECT * FROM timeframes WHERE timeframe_id = ?;";
	private static final String GET_TIMEFRAMES_QUERY = "SELECT * FROM timeframes;";
	private static final String DELETE_TIMEFRAME_BY_ID_QUERY = "DELETE FROM timeframes WHERE timeframe_id = ?;";
	private static final String UPDATE_TIMEFRAME_QUERY = "UPDATE timeframes SET timeframe_sequance = ?, start_time = ?, end_time = ? WHERE timeframe_id = ?";

	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert jdbcInsert;

	public JdbcTimeframeDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(TIMEFRAMES_TABLE_NAME)
				.usingGeneratedKeyColumns(TIMEFRAME_ID);
	}

	@Override
	public void create(Timeframe timeframe) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put(TIMEFRAME_SEQUANCE, timeframe.getSequance());
		parameters.put(TIMEFRAME_START_TIME, timeframe.getStartTime());
		parameters.put(TIMEFRAME_END_TIME, timeframe.getEndTime());
		timeframe.setId(jdbcInsert.executeAndReturnKey(parameters).longValue());
	}

	@Override
	public Timeframe findById(Long timeframeId) {
		return jdbcTemplate
				.queryForObject(FIND_TIMEFRAME_BY_ID_QUERY, new Object[] { timeframeId }, new TimeframeMapper());
	}

	@Override
	public List<Timeframe> getAll() {
		return jdbcTemplate.query(GET_TIMEFRAMES_QUERY, new TimeframeMapper());
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
