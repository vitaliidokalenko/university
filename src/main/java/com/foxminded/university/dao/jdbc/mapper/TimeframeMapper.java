package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.model.Timeframe;

@Component
public class TimeframeMapper implements RowMapper<Timeframe> {

	public static final String TIMEFRAME_ID = "id";
	public static final String TIMEFRAME_SEQUENCE = "sequence";
	public static final String START_TIME = "start_time";
	public static final String END_TIME = "end_time";

	@Override
	public Timeframe mapRow(ResultSet rs, int rowNum) throws SQLException {
		Timeframe timeframe = new Timeframe();
		timeframe.setId(rs.getLong(TIMEFRAME_ID));
		timeframe.setSequence(rs.getInt(TIMEFRAME_SEQUENCE));
		timeframe.setStartTime(rs.getObject(START_TIME, LocalTime.class));
		timeframe.setEndTime(rs.getObject(END_TIME, LocalTime.class));
		return timeframe;
	}
}
