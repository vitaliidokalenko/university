package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.foxminded.university.model.Group;

public class GroupMapper implements RowMapper<Group> {

	private static final String GROUP_NAME = "group_name";
	private static final String GROUP_ID = "group_id";

	@Override
	public Group mapRow(ResultSet result, int rowNum) throws SQLException {
		Group group = new Group(result.getString(GROUP_NAME));
		group.setId(result.getLong(GROUP_ID));
		return group;
	}
}
