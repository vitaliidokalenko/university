package com.foxminded.university.dao.hibernate;

import java.time.LocalDate;
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

import com.foxminded.university.dao.LessonDao;
import com.foxminded.university.dao.exception.DaoException;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Lesson;
import com.foxminded.university.model.Room;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.model.Timeframe;

@Component
public class HibernateLessonDao implements LessonDao {

	private SessionFactory sessionFactory;

	public HibernateLessonDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Lesson lesson) {
		try {
			sessionFactory.getCurrentSession().save(lesson);
		} catch (Exception e) {
			throw new DaoException("Could not create lesson: " + lesson, e);
		}
	}

	@Override
	public Optional<Lesson> findById(Long id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			root.fetch("groups", JoinType.LEFT);
			query.select(root).where(builder.equal(root.get("id"), id));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException("Could not get lesson by id: " + id, e);
		}
	}

	@Override
	public List<Lesson> getAll() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Lesson> query = session.getCriteriaBuilder().createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			query.select(root);
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get lessons", e);
		}
	}

	@Override
	public void update(Lesson lesson) {
		try {
			sessionFactory.getCurrentSession().merge(lesson);
		} catch (Exception e) {
			throw new DaoException("Could not update lesson: " + lesson, e);
		}
	}

	@Override
	public void delete(Lesson lesson) {
		try {
			sessionFactory.getCurrentSession().delete(lesson);
		} catch (Exception e) {
			throw new DaoException("Could not delete lesson: " + lesson, e);
		}
	}

	@Override
	public long count() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			query.select(builder.count(query.from(Lesson.class)));
			return session.createQuery(query).getSingleResult();
		} catch (Exception e) {
			throw new DaoException("Could not get amount of lessons", e);
		}
	}

	@Override
	public Page<Lesson> getAllPage(Pageable pageable) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Lesson> query = session.getCriteriaBuilder().createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			root.fetch("groups", JoinType.LEFT);
			query.select(root);
			List<Lesson> lessons = session.createQuery(query)
					.setFirstResult((int) pageable.getOffset())
					.setMaxResults(pageable.getPageSize())
					.getResultList();
			return new PageImpl<>(lessons, pageable, count());
		} catch (Exception e) {
			throw new DaoException("Could not get lessons", e);
		}
	}

	@Override
	public Optional<Lesson> getByGroupIdAndDateAndTimeframe(Long groupId, LocalDate date, Timeframe timeframe) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			root.fetch("groups", JoinType.LEFT);
			query.select(root)
					.where(builder.and(builder.equal(root.join("groups").get("id"), groupId),
							builder.equal(root.get("date"), date),
							builder.equal(root.get("timeframe"), timeframe)));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException(
					"Could not get lesson by group id: " + groupId + ", date: " + date + ", timeframe: " + timeframe,
					e);
		}
	}

	@Override
	public List<Lesson> getByTimeframe(Timeframe timeframe) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			query.select(root).where(builder.equal(root.get("timeframe"), timeframe));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get lessons by timeframe: " + timeframe, e);
		}
	}

	@Override
	public List<Lesson> getByCourse(Course course) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			query.select(root).where(builder.equal(root.get("course"), course));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get lessons by course: " + course, e);
		}
	}

	@Override
	public Optional<Lesson> getByTeacherAndDateAndTimeframe(Teacher teacher, LocalDate date, Timeframe timeframe) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			root.fetch("groups", JoinType.LEFT);
			query.select(root)
					.where(builder.and(builder.equal(root.get("teacher"), teacher),
							builder.equal(root.get("date"), date),
							builder.equal(root.get("timeframe"), timeframe)));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException(
					"Could not get lesson by teacher: " + teacher + ", date: " + date + ", timeframe: " + timeframe, e);
		}
	}

	@Override
	public Optional<Lesson> getByRoomAndDateAndTimeframe(Room room, LocalDate date, Timeframe timeframe) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			query.select(root)
					.where(builder.and(builder.equal(root.get("room"), room),
							builder.equal(root.get("date"), date),
							builder.equal(root.get("timeframe"), timeframe)));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException(
					"Could not get lesson by room: " + room + ", date: " + date + ", timeframe: " + timeframe, e);
		}
	}

	@Override
	public List<Lesson> getByTeacherIdAndDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			query.select(root)
					.where(builder.equal(root.get("teacher").get("id"), teacherId),
							builder.between(root.get("date"), startDate, endDate));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException(
					"Could not get lessons by teacher id: " + teacherId + ", between " + startDate + " and " + endDate,
					e);
		}
	}

	@Override
	public List<Lesson> getByGroupIdAndDateBetween(Long groupId, LocalDate startDate, LocalDate endDate) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
			Root<Lesson> root = query.from(Lesson.class);
			query.select(root)
					.where(builder.equal(root.join("groups").get("id"), groupId),
							builder.between(root.get("date"), startDate, endDate));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException(
					"Could not get lessons by group id: " + groupId + ", between " + startDate + " and " + endDate,
					e);
		}
	}
}
