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
import com.foxminded.university.dao.exception.DaoException;
import com.foxminded.university.model.Teacher;

@Component
public class HibernateTeacherDao implements TeacherDao {

	private SessionFactory sessionFactory;

	public HibernateTeacherDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Teacher teacher) {
		try {
			sessionFactory.getCurrentSession().save(teacher);
		} catch (Exception e) {
			throw new DaoException("Could not create teacher: " + teacher, e);
		}
	}

	@Override
	public Optional<Teacher> findById(Long id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
			Root<Teacher> root = query.from(Teacher.class);
			root.fetch("courses", JoinType.LEFT);
			query.select(root).where(builder.equal(root.get("id"), id));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException("Could not get teacher by id: " + id, e);
		}
	}

	@Override
	public List<Teacher> getAll() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Teacher> query = session.getCriteriaBuilder().createQuery(Teacher.class);
			query.select(query.from(Teacher.class));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get teachers", e);
		}
	}

	@Override
	public void update(Teacher teacher) {
		try {
			sessionFactory.getCurrentSession().merge(teacher);
		} catch (Exception e) {
			throw new DaoException("Could not update teacher: " + teacher, e);
		}
	}

	@Override
	public void delete(Teacher teacher) {
		try {
			sessionFactory.getCurrentSession().delete(teacher);
		} catch (Exception e) {
			throw new DaoException("Could not delete teacher: " + teacher, e);
		}
	}

	@Override
	public long count() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			query.select(builder.count(query.from(Teacher.class)));
			return session.createQuery(query).getSingleResult();
		} catch (Exception e) {
			throw new DaoException("Could not get amount of teachers", e);
		}
	}

	@Override
	public Page<Teacher> getAllPage(Pageable pageable) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Teacher> query = session.getCriteriaBuilder().createQuery(Teacher.class);
			query.select(query.from(Teacher.class));
			List<Teacher> etachers = session.createQuery(query)
					.setFirstResult((int) pageable.getOffset())
					.setMaxResults(pageable.getPageSize())
					.getResultList();
			return new PageImpl<>(etachers, pageable, count());
		} catch (Exception e) {
			throw new DaoException("Could not get teachers", e);
		}

	}

	@Override
	public List<Teacher> getByCourseId(Long courseId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Teacher> query = builder.createQuery(Teacher.class);
			Root<Teacher> root = query.from(Teacher.class);
			root.fetch("courses", JoinType.LEFT);
			query.select(root).where(builder.equal(root.join("courses").get("id"), courseId));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get teachers by course id: " + courseId, e);
		}
	}
}
