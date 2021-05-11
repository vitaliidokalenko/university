package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TeacherDao;
import com.foxminded.university.model.Teacher;

@Component
public class HibernateTeacherDao implements TeacherDao {

	private SessionFactory sessionFactory;

	public HibernateTeacherDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Teacher teacher) {
		sessionFactory.getCurrentSession().save(teacher);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Teacher> findById(Long id) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getTeacherById")
				.setParameter("id", id)
				.uniqueResultOptional();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> getAll() {
		return sessionFactory.getCurrentSession().getNamedQuery("getAllTeachers").getResultList();
	}

	@Override
	public void update(Teacher teacher) {
		sessionFactory.getCurrentSession().merge(teacher);
	}

	@Override
	public void delete(Teacher teacher) {
		sessionFactory.getCurrentSession().delete(teacher);
	}

	@Override
	public long count() {
		return (long) sessionFactory.getCurrentSession().getNamedQuery("countTeachers").getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Teacher> getAllPage(Pageable pageable) {
		List<Teacher> teachers = sessionFactory.getCurrentSession()
				.getNamedQuery("getAllTeachers")
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(teachers, pageable, count());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> getByCourseId(Long courseId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getTeachersByCourseId")
				.setParameter("id", courseId)
				.getResultList();
	}
}
