package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
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

	@Override
	public Optional<Teacher> findById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
		Root<Teacher> root = query.from(Teacher.class);
		root.fetch("courses", JoinType.LEFT);
		query.select(root).where(builder.equal(root.get("id"), id));
		return session.createQuery(query).uniqueResultOptional();
	}

	@Override
	public List<Teacher> getAll() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Teacher> query = session.getCriteriaBuilder().createQuery(Teacher.class);
		query.select(query.from(Teacher.class));
		return session.createQuery(query).getResultList();
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
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		query.select(builder.count(query.from(Teacher.class)));
		return session.createQuery(query).getSingleResult();
	}

	@Override
	public Page<Teacher> getAllPage(Pageable pageable) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Teacher> query = session.getCriteriaBuilder().createQuery(Teacher.class);
		query.select(query.from(Teacher.class));
		List<Teacher> etachers = session.createQuery(query)
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(etachers, pageable, count());
	}

	@Override
	public List<Teacher> getByCourseId(Long courseId) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
		Root<Teacher> root = query.from(Teacher.class);
		root.fetch("courses", JoinType.LEFT);
		query.select(root).where(builder.equal(root.join("courses").get("id"), courseId));
		return session.createQuery(query).getResultList();
	}
}
