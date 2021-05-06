package com.foxminded.university.dao.hibernate;

import java.time.LocalDate;
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

import com.foxminded.university.dao.LessonDao;
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
		sessionFactory.getCurrentSession().save(lesson);
	}

	@Override
	public Optional<Lesson> findById(Long id) {
		return Optional.ofNullable(sessionFactory.getCurrentSession().get(Lesson.class, id));
	}

	@Override
	public List<Lesson> getAll() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Lesson> query = session.getCriteriaBuilder().createQuery(Lesson.class);
		query.select(query.from(Lesson.class));
		return session.createQuery(query).getResultList();
	}

	@Override
	public void update(Lesson lesson) {
		sessionFactory.getCurrentSession().merge(lesson);
	}

	@Override
	public void delete(Lesson lesson) {
		sessionFactory.getCurrentSession().delete(lesson);
	}

	@Override
	public long count() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		query.select(builder.count(query.from(Lesson.class)));
		return session.createQuery(query).getSingleResult();
	}

	@Override
	public Page<Lesson> getAllPage(Pageable pageable) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Lesson> query = session.getCriteriaBuilder().createQuery(Lesson.class);
		query.select(query.from(Lesson.class));
		List<Lesson> lessons = session.createQuery(query)
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(lessons, pageable, count());
	}

	@Override
	public Optional<Lesson> getByGroupIdAndDateAndTimeframe(Long groupId, LocalDate date, Timeframe timeframe) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
		Root<Lesson> root = query.from(Lesson.class);
		query.select(root)
				.where(builder.and(builder.equal(root.join("groups").get("id"), groupId),
						builder.equal(root.get("date"), date),
						builder.equal(root.get("timeframe"), timeframe)));
		return session.createQuery(query).uniqueResultOptional();
	}

	@Override
	public List<Lesson> getByTimeframe(Timeframe timeframe) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
		Root<Lesson> root = query.from(Lesson.class);
		query.select(root).where(builder.equal(root.get("timeframe"), timeframe));
		return session.createQuery(query).getResultList();
	}

	@Override
	public List<Lesson> getByCourse(Course course) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
		Root<Lesson> root = query.from(Lesson.class);
		query.select(root).where(builder.equal(root.get("course"), course));
		return session.createQuery(query).getResultList();
	}

	@Override
	public Optional<Lesson> getByTeacherAndDateAndTimeframe(Teacher teacher, LocalDate date, Timeframe timeframe) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
		Root<Lesson> root = query.from(Lesson.class);
		query.select(root)
				.where(builder.and(builder.equal(root.get("teacher"), teacher),
						builder.equal(root.get("date"), date),
						builder.equal(root.get("timeframe"), timeframe)));
		return session.createQuery(query).uniqueResultOptional();
	}

	@Override
	public Optional<Lesson> getByRoomAndDateAndTimeframe(Room room, LocalDate date, Timeframe timeframe) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
		Root<Lesson> root = query.from(Lesson.class);
		query.select(root)
				.where(builder.and(builder.equal(root.get("room"), room),
						builder.equal(root.get("date"), date),
						builder.equal(root.get("timeframe"), timeframe)));
		return session.createQuery(query).uniqueResultOptional();
	}

	@Override
	public List<Lesson> getByTeacherIdAndDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
		Root<Lesson> root = query.from(Lesson.class);
		query.select(root)
				.where(builder.equal(root.get("teacher").get("id"), teacherId),
						builder.between(root.get("date"), startDate, endDate));
		return session.createQuery(query).getResultList();
	}

	@Override
	public List<Lesson> getByGroupIdAndDateBetween(Long groupId, LocalDate startDate, LocalDate endDate) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Lesson> query = builder.createQuery(Lesson.class);
		Root<Lesson> root = query.from(Lesson.class);
		query.select(root)
				.where(builder.equal(root.join("groups").get("id"), groupId),
						builder.between(root.get("date"), startDate, endDate));
		return session.createQuery(query).getResultList();
	}
}
