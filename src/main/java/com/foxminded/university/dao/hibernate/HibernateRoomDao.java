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

import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.dao.exception.DaoException;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Room;

@Component
public class HibernateRoomDao implements RoomDao {

	private SessionFactory sessionFactory;

	public HibernateRoomDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Room room) {
		try {
			sessionFactory.getCurrentSession().save(room);
		} catch (Exception e) {
			throw new DaoException("Could not create room: " + room, e);
		}
	}

	@Override
	public Optional<Room> findById(Long id) {
		try {
			return Optional.ofNullable(sessionFactory.getCurrentSession().get(Room.class, id));
		} catch (Exception e) {
			throw new DaoException("Could not get room by id: " + id, e);
		}
	}

	@Override
	public List<Room> getAll() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Room> query = session.getCriteriaBuilder().createQuery(Room.class);
			query.select(query.from(Room.class));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get rooms", e);
		}
	}

	@Override
	public void update(Room room) {
		try {
			sessionFactory.getCurrentSession().merge(room);
		} catch (Exception e) {
			throw new DaoException("Cold not update room: " + room, e);
		}
	}

	@Override
	public void delete(Room room) {
		try {
			sessionFactory.getCurrentSession().delete(room);
		} catch (Exception e) {
			throw new DaoException("Could not delete room: " + room, e);
		}
	}

	@Override
	public long count() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			query.select(builder.count(query.from(Room.class)));
			return session.createQuery(query).getSingleResult();
		} catch (Exception e) {
			throw new DaoException("Could not get amount of rooms", e);
		}
	}

	@Override
	public Page<Room> getAllPage(Pageable pageable) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Room> query = session.getCriteriaBuilder().createQuery(Room.class);
			query.select(query.from(Room.class));
			List<Room> rooms = session.createQuery(query)
					.setFirstResult((int) pageable.getOffset())
					.setMaxResults(pageable.getPageSize())
					.getResultList();
			return new PageImpl<>(rooms, pageable, count());
		} catch (Exception e) {
			throw new DaoException("Could not get rooms", e);
		}
	}

	@Override
	public List<Room> getByCourseId(Long courseId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Room> query = builder.createQuery(Room.class);
			Root<Course> root = query.from(Course.class);
			query.select(root.get("rooms")).where(builder.equal(root.get("id"), courseId));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get rooms by course id: " + courseId, e);
		}
	}

	@Override
	public Optional<Room> findByName(String name) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Room> query = builder.createQuery(Room.class);
			Root<Room> root = query.from(Room.class);
			query.select(root).where(builder.equal(root.get("name"), name));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException("Could not get room by name: " + name, e);
		}
	}
}
