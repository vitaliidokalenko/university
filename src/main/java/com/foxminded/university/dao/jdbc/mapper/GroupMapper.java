package com.foxminded.university.dao.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.foxminded.university.model.Group;

@Component
public class GroupMapper implements RowMapper<Group> {

	public static final String GROUP_ID = "id";
	public static final String GROUP_NAME = "name";

	@Override
	public Group mapRow(ResultSet result, int rowNum) throws SQLException {
		Group group = new Group(result.getString(GROUP_NAME));
		group.setId(result.getLong(GROUP_ID));
		return group;
	}
}
