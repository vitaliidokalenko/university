package com.foxminded.university.dao;

import java.util.List;

public interface GenericDao<T> {

	public void create(T entity);

	public T findById(Long id);

	public List<T> getAll();

	public void update(T entity);

	public void deleteById(Long id);
}
