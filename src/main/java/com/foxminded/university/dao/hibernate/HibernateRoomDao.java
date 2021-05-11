package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;

@Component
public class HibernateRoomDao implements RoomDao {

	private SessionFactory sessionFactory;

	public HibernateRoomDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Room room) {
		sessionFactory.getCurrentSession().save(room);
	}

	@Override
	public Optional<Room> findById(Long id) {
		return Optional.ofNullable(sessionFactory.getCurrentSession().get(Room.class, id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getAll() {
		return sessionFactory.getCurrentSession().getNamedQuery("getAllRooms").getResultList();
	}

	@Override
	public void update(Room room) {
		sessionFactory.getCurrentSession().merge(room);
	}

	@Override
	public void delete(Room room) {
		sessionFactory.getCurrentSession().delete(room);
	}

	@Override
	public long count() {
		return (long) sessionFactory.getCurrentSession().getNamedQuery("countRooms").getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<Room> getAllPage(Pageable pageable) {
		List<Room> rooms = sessionFactory.getCurrentSession()
				.getNamedQuery("getAllRooms")
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(rooms, pageable, count());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getByCourseId(Long courseId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getRoomsByCourseId")
				.setParameter("id", courseId)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Optional<Room> findByName(String name) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("findRoomByName")
				.setParameter("name", name)
				.uniqueResultOptional();
	}
}
