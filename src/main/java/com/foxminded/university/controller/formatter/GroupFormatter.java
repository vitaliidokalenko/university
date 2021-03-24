package com.foxminded.university.controller.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import com.foxminded.university.model.Group;

public class GroupFormatter implements Formatter<Group> {

	@Override
	public String print(Group group, Locale locale) {
		return Long.toString(group.getId());
	}

	@Override
	public Group parse(String id, Locale locale) throws ParseException {
		Group group = new Group();
		group.setId(Long.valueOf(id));
		return group;
	}

}
