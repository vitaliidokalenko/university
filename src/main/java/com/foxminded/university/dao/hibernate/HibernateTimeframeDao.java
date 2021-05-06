package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.dao.exception.DaoException;
import com.foxminded.university.model.Timeframe;

@Component
public class HibernateTimeframeDao implements TimeframeDao {

	private SessionFactory sessionFactory;

	public HibernateTimeframeDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Timeframe timeframe) {
		try {
			sessionFactory.getCurrentSession().save(timeframe);
		} catch (Exception e) {
			throw new DaoException("Could not create timeframe: " + timeframe, e);
		}
	}

	@Override
	public Optional<Timeframe> findById(Long id) {
		try {
			return Optional.ofNullable(sessionFactory.getCurrentSession().get(Timeframe.class, id));
		} catch (Exception e) {
			throw new DaoException("Could not get timeframe by id: " + id, e);
		}
	}

	@Override
	public List<Timeframe> getAll() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Timeframe> query = session.getCriteriaBuilder().createQuery(Timeframe.class);
			query.select(query.from(Timeframe.class));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get timeframes", e);
		}
	}

	@Override
	public void update(Timeframe timeframe) {
		try {
			sessionFactory.getCurrentSession().merge(timeframe);
		} catch (Exception e) {
			throw new DaoException("Could not update timeframe: " + timeframe, e);
		}
	}

	@Override
	public void delete(Timeframe timeframe) {
		try {
			sessionFactory.getCurrentSession().delete(timeframe);
		} catch (Exception e) {
			throw new DaoException("Could not delete timeframe: " + timeframe, e);
		}
	}

	@Override
	public long count() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			query.select(builder.count(query.from(Timeframe.class)));
			return session.createQuery(query).getSingleResult();
		} catch (Exception e) {
			throw new DaoException("Could not get amount of timeframes", e);
		}
	}

	@Override
	public Page<Timeframe> getAllPage(Pageable pageable) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Timeframe> query = session.getCriteriaBuilder().createQuery(Timeframe.class);
			query.select(query.from(Timeframe.class));
			List<Timeframe> timeframes = session.createQuery(query)
					.setFirstResult((int) pageable.getOffset())
					.setMaxResults(pageable.getPageSize())
					.getResultList();
			return new PageImpl<>(timeframes, pageable, count());
		} catch (Exception e) {
			throw new DaoException("Could not get timeframes", e);
		}
	}

	@Override
	public Optional<Timeframe> findBySequence(int sequence) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Timeframe> query = builder.createQuery(Timeframe.class);
			Root<Timeframe> root = query.from(Timeframe.class);
			query.select(root).where(builder.equal(root.get("sequence"), sequence));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException("Could not get timeframe by sequence: " + sequence, e);
		}
	}
}
