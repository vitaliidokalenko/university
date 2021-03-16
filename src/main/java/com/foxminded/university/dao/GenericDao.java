package com.foxminded.university.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericDao<T> {

	public void create(T entity);

	public Optional<T> findById(Long id);

	public List<T> getAll();

	public void update(T entity);

	public void deleteById(Long id);

	public int count();

	public Page<T> getAllPage(Pageable pageable);
}
