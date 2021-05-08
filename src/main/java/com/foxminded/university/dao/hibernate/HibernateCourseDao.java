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

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Student;
import com.foxminded.university.model.Teacher;

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
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Course> query = builder.createQuery(Course.class);
		Root<Course> root = query.from(Course.class);
		root.fetch("rooms", JoinType.LEFT);
		query.select(root).where(builder.equal(root.get("id"), id));
		return session.createQuery(query).uniqueResultOptional();
	}

	@Override
	public List<Course> getAll() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Course> query = session.getCriteriaBuilder().createQuery(Course.class);
		query.select(query.from(Course.class));
		return session.createQuery(query).getResultList();
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
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		query.select(builder.count(query.from(Course.class)));
		return session.createQuery(query).getSingleResult();
	}

	@Override
	public Page<Course> getAllPage(Pageable pageable) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Course> query = session.getCriteriaBuilder().createQuery(Course.class);
		query.select(query.from(Course.class));
		List<Course> courses = session.createQuery(query)
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(courses, pageable, count());
	}

	@Override
	public List<Course> getByRoomId(Long roomId) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Course> query = builder.createQuery(Course.class);
		Root<Course> root = query.from(Course.class);
		query.select(root).where(builder.equal(root.join("rooms").get("id"), roomId));
		return session.createQuery(query).getResultList();
	}

	@Override
	public List<Course> getByStudentId(Long studentId) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Course> query = builder.createQuery(Course.class);
		Root<Student> root = query.from(Student.class);
		query.select(root.get("courses")).where(builder.equal(root.get("id"), studentId));
		return session.createQuery(query).getResultList();
	}

	@Override
	public List<Course> getByTeacherId(Long teacherId) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Course> query = builder.createQuery(Course.class);
		Root<Teacher> root = query.from(Teacher.class);
		query.select(root.get("courses")).where(builder.equal(root.get("id"), teacherId));
		return session.createQuery(query).getResultList();
	}

	@Override
	public Optional<Course> findByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Course> query = builder.createQuery(Course.class);
		Root<Course> root = query.from(Course.class);
		query.select(root).where(builder.equal(root.get("name"), name));
		return session.createQuery(query).uniqueResultOptional();
	}
}
