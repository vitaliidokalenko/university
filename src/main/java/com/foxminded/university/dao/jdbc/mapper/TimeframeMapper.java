package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.model.Timeframe;

public class TimeframeMapper implements RowMapper<Timeframe> {

	public static final String TIMEFRAME_ID = "id";
	public static final String TIMEFRAME_SEQUANCE = "sequance";
	public static final String START_TIME = "start_time";
	public static final String END_TIME = "end_time";

	@Override
	public Timeframe mapRow(ResultSet rs, int rowNum) throws SQLException {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(rs.getLong(TIMEFRAME_ID));
		timeframe.setSequance(rs.getInt(TIMEFRAME_SEQUANCE));
		timeframe.setStartTime(rs.getObject(START_TIME, LocalTime.class));
		timeframe.setEndTime(rs.getObject(END_TIME, LocalTime.class));
		return timeframe;
	}
}
