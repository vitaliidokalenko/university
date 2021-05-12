package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;

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
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getGroupById")
				.setParameter("id", id)
				.uniqueResultOptional();
	}

	@Override
	public List<Group> getAll() {
		return sessionFactory.getCurrentSession().getNamedQuery("getAllGroups").getResultList();
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
		return (long) sessionFactory.getCurrentSession().getNamedQuery("countGroups").getSingleResult();
	}

	@Override
	public Page<Group> getAllPage(Pageable pageable) {
		List<Group> groups = sessionFactory.getCurrentSession()
				.getNamedQuery("getAllGroups")
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(groups, pageable, count());
	}

	@Override
	public List<Group> getByLessonId(Long lessonId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getGroupsByLessonId")
				.setParameter("id", lessonId)
				.getResultList();
	}

	@Override
	public Optional<Group> findByName(String name) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("findGroupByName")
				.setParameter("name", name)
				.uniqueResultOptional();
	}
}
