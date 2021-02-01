package com.foxminded.university.dao;

import java.util.List;

import com.foxminded.university.model.Teacher;

public interface TeacherDao extends GenericDao<Teacher> {

	public List<Teacher> getByCourseId(Long courseId);
}
