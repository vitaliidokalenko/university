package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.TimeframeDao;
import com.foxminded.university.model.Timeframe;

@Component
public class HibernateTimeframeDao implements TimeframeDao {

	private SessionFactory sessionFactory;

	public HibernateTimeframeDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Timeframe timeframe) {
		sessionFactory.getCurrentSession().save(timeframe);
	}

	@Override
	public Optional<Timeframe> findById(Long id) {
		return Optional.ofNullable(sessionFactory.getCurrentSession().get(Timeframe.class, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Timeframe> getAll() {
		return sessionFactory.getCurrentSession().getNamedQuery("getAllTimeframes").getResultList();
	}

	@Override
	public void update(Timeframe timeframe) {
		sessionFactory.getCurrentSession().merge(timeframe);
	}

	@Override
	public void delete(Timeframe timeframe) {
		sessionFactory.getCurrentSession().delete(timeframe);
	}

	@Override
	public long count() {
		return (long) sessionFactory.getCurrentSession().getNamedQuery("countTimeframes").getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Timeframe> getAllPage(Pageable pageable) {
		List<Timeframe> timeframes = sessionFactory.getCurrentSession()
				.getNamedQuery("getAllTimeframes")
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(timeframes, pageable, count());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Timeframe> findBySequence(int sequence) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("findTimeframeBySequence")
				.setParameter("sequence", sequence)
				.uniqueResultOptional();
	}
}
