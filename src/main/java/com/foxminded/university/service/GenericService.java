package com.foxminded.university.service;

import java.util.List;

public interface GenericService<T> {

	public void create(T entity);

	public T findById(Long id);

	public List<T> getAll();

	public void update(T entity);

	public void deleteById(Long id);
	
	public boolean existsById(Long id);
}
