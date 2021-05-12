package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;

@Component
public class HibernateCourseDao implements CourseDao {

	private SessionFactory sessionFactory;

	public HibernateCourseDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Course course) {
		sessionFactory.getCurrentSession().save(course);
	}

	@Override
	public Optional<Course> findById(Long id) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getCourseById")
				.setParameter("id", id)
				.uniqueResultOptional();
	}

	@Override
	public List<Course> getAll() {
		return sessionFactory.getCurrentSession().getNamedQuery("getAllCourses").getResultList();
	}

	@Override
	public void update(Course course) {
		sessionFactory.getCurrentSession().merge(course);
	}

	@Override
	public void delete(Course course) {
		sessionFactory.getCurrentSession().delete(course);
	}

	@Override
	public long count() {
		return (long) sessionFactory.getCurrentSession().getNamedQuery("countCourses").getSingleResult();
	}

	@Override
	public Page<Course> getAllPage(Pageable pageable) {
		List<Course> courses = sessionFactory.getCurrentSession()
				.getNamedQuery("getAllCourses")
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(courses, pageable, count());
	}

	@Override
	public List<Course> getByRoomId(Long roomId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getCoursesByRoomId")
				.setParameter("id", roomId)
				.getResultList();
	}

	@Override
	public List<Course> getByStudentId(Long studentId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getCoursesByStudentId")
				.setParameter("id", studentId)
				.getResultList();
	}

	@Override
	public List<Course> getByTeacherId(Long teacherId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getCoursesByTeacherId")
				.setParameter("id", teacherId)
				.getResultList();
	}

	@Override
	public Optional<Course> findByName(String name) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("findCourseByName")
				.setParameter("name", name)
				.uniqueResultOptional();
	}
}
