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
import com.foxminded.university.dao.exception.DaoException;
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
		try {
			sessionFactory.getCurrentSession().save(course);
		} catch (Exception e) {
			throw new DaoException("Could not create course: " + course, e);
		}
	}

	@Override
	public Optional<Course> findById(Long id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Course> query = builder.createQuery(Course.class);
			Root<Course> root = query.from(Course.class);
			root.fetch("rooms", JoinType.LEFT);
			query.select(root).where(builder.equal(root.get("id"), id));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException("Could not get course by id: " + id, e);
		}
	}

	@Override
	public List<Course> getAll() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Course> query = session.getCriteriaBuilder().createQuery(Course.class);
			query.select(query.from(Course.class));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get courses", e);
		}
	}

	@Override
	public void update(Course course) {
		try {
			sessionFactory.getCurrentSession().merge(course);
		} catch (Exception e) {
			throw new DaoException("Could not update course: " + course, e);
		}
	}

	@Override
	public void delete(Course course) {
		try {
			sessionFactory.getCurrentSession().delete(course);
		} catch (Exception e) {
			throw new DaoException("Could not delete course: " + course, e);
		}
	}

	@Override
	public long count() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			query.select(builder.count(query.from(Course.class)));
			return session.createQuery(query).getSingleResult();
		} catch (Exception e) {
			throw new DaoException("Could not get amount of courses", e);
		}
	}

	@Override
	public Page<Course> getAllPage(Pageable pageable) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Course> query = session.getCriteriaBuilder().createQuery(Course.class);
			query.select(query.from(Course.class));
			List<Course> courses = session.createQuery(query)
					.setFirstResult((int) pageable.getOffset())
					.setMaxResults(pageable.getPageSize())
					.getResultList();
			return new PageImpl<>(courses, pageable, count());
		} catch (Exception e) {
			throw new DaoException("Could not get courses", e);
		}
	}

	@Override
	public List<Course> getByRoomId(Long roomId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Course> query = builder.createQuery(Course.class);
			Root<Course> root = query.from(Course.class);
			query.select(root).where(builder.equal(root.join("rooms").get("id"), roomId));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get courses by room id: " + roomId, e);
		}
	}

	@Override
	public List<Course> getByStudentId(Long studentId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Course> query = builder.createQuery(Course.class);
			Root<Student> root = query.from(Student.class);
			query.select(root.get("courses")).where(builder.equal(root.get("id"), studentId));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get courses by student id: " + studentId, e);
		}
	}

	@Override
	public List<Course> getByTeacherId(Long teacherId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Course> query = builder.createQuery(Course.class);
			Root<Teacher> root = query.from(Teacher.class);
			query.select(root.get("courses")).where(builder.equal(root.get("id"), teacherId));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get courses by teacher id: " + teacherId, e);
		}
	}

	@Override
	public Optional<Course> findByName(String name) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Course> query = builder.createQuery(Course.class);
			Root<Course> root = query.from(Course.class);
			query.select(root).where(builder.equal(root.get("name"), name));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException("Could not get course by name: " + name, e);
		}

	}
}
