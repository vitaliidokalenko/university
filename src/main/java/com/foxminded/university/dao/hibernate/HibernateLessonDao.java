package com.foxminded.university.dao.hibernate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.LessonDao;
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
	public void update(Lesson lesson) {
		sessionFactory.getCurrentSession().merge(lesson);
	}

	@Override
	public void delete(Lesson lesson) {
		sessionFactory.getCurrentSession().delete(lesson);
	}

	@Override
	public long count() {
		return (long) sessionFactory.getCurrentSession().getNamedQuery("countLessons").getSingleResult();
	}

	@Override
	public Page<Lesson> getAllPage(Pageable pageable) {
		Session session = sessionFactory.getCurrentSession();
		List<Lesson> lessons = session.getNamedQuery("getAllLessons")
				.setHint("javax.persistence.loadgraph", session.getEntityGraph("Lesson.groups"))
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(lessons, pageable, count());
	}

	@Override
	public Optional<Lesson> getByGroupIdAndDateAndTimeframe(Long groupId, LocalDate date, Timeframe timeframe) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getLessonByGroupIdAndDateAndTimeframe")
				.setParameter("id", groupId)
				.setParameter("date", date)
				.setParameter("timeframe", timeframe)
				.uniqueResultOptional();
	}

	@Override
	public Optional<Lesson> getByTeacherAndDateAndTimeframe(Teacher teacher, LocalDate date, Timeframe timeframe) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getLessonByTeacherAndDateAndTimeframe")
				.setParameter("teacher", teacher)
				.setParameter("date", date)
				.setParameter("timeframe", timeframe)
				.uniqueResultOptional();
	}

	@Override
	public Optional<Lesson> getByRoomAndDateAndTimeframe(Room room, LocalDate date, Timeframe timeframe) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getLessonByRoomAndDateAndTimeframe")
				.setParameter("room", room)
				.setParameter("date", date)
				.setParameter("timeframe", timeframe)
				.uniqueResultOptional();
	}

	@Override
	public List<Lesson> getByTeacherIdAndDateBetween(Long teacherId, LocalDate startDate, LocalDate endDate) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getLessonsByTeacherIdAndDateBetween")
				.setParameter("id", teacherId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
	}

	@Override
	public List<Lesson> getByGroupIdAndDateBetween(Long groupId, LocalDate startDate, LocalDate endDate) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getLessonsByGroupIdAndDateBetween")
				.setParameter("id", groupId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
	}
}
