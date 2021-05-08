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

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Lesson;

@Component
public class HibernateGroupDao implements GroupDao {

	private SessionFactory sessionFactory;

	public HibernateGroupDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Group group) {
		sessionFactory.getCurrentSession().save(group);
	}

	@Override
	public Optional<Group> findById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Group> query = builder.createQuery(Group.class);
		Root<Group> root = query.from(Group.class);
		root.fetch("students", JoinType.LEFT);
		query.select(root).where(builder.equal(root.get("id"), id));
		return session.createQuery(query).uniqueResultOptional();
	}

	@Override
	public List<Group> getAll() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Group> query = session.getCriteriaBuilder().createQuery(Group.class);
		query.select(query.from(Group.class));
		return session.createQuery(query).getResultList();
	}

	@Override
	public void update(Group group) {
		sessionFactory.getCurrentSession().merge(group);
	}

	@Override
	public void delete(Group group) {
		sessionFactory.getCurrentSession().delete(group);
	}

	@Override
	public long count() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		query.select(builder.count(query.from(Group.class)));
		return session.createQuery(query).getSingleResult();
	}

	@Override
	public Page<Group> getAllPage(Pageable pageable) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Group> query = session.getCriteriaBuilder().createQuery(Group.class);
		query.select(query.from(Group.class));
		List<Group> groups = session.createQuery(query)
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(groups, pageable, count());
	}

	@Override
	public List<Group> getByLessonId(Long lessonId) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Group> query = builder.createQuery(Group.class);
		Root<Lesson> root = query.from(Lesson.class);
		query.select(root.get("groups")).where(builder.equal(root.get("id"), lessonId));
		return session.createQuery(query).getResultList();
	}

	@Override
	public Optional<Group> findByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Group> query = builder.createQuery(Group.class);
		Root<Group> root = query.from(Group.class);
		query.select(root).where(builder.equal(root.get("name"), name));
		return session.createQuery(query).uniqueResultOptional();
	}
}
