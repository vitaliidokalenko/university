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

import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.dao.exception.DaoException;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@Component
public class HibernateStudentDao implements StudentDao {

	private SessionFactory sessionFactory;

	public HibernateStudentDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void create(Student student) {
		try {
			sessionFactory.getCurrentSession().save(student);
		} catch (Exception e) {
			throw new DaoException("Could not create student: " + student, e);
		}
	}

	@Override
	public Optional<Student> findById(Long id) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Student> query = builder.createQuery(Student.class);
			Root<Student> root = query.from(Student.class);
			root.fetch("courses", JoinType.LEFT);
			query.select(root).where(builder.equal(root.get("id"), id));
			return session.createQuery(query).uniqueResultOptional();
		} catch (Exception e) {
			throw new DaoException("Could not get student by id: " + id, e);
		}
	}

	@Override
	public List<Student> getAll() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Student> query = session.getCriteriaBuilder().createQuery(Student.class);
			query.select(query.from(Student.class));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get students", e);
		}
	}

	@Override
	public void update(Student student) {
		try {
			sessionFactory.getCurrentSession().merge(student);
		} catch (Exception e) {
			throw new DaoException("Could not update student: " + student, e);
		}
	}

	@Override
	public void delete(Student student) {
		try {
			sessionFactory.getCurrentSession().delete(student);
		} catch (Exception e) {
			throw new DaoException("Could not delete student: " + student, e);
		}
	}

	@Override
	public long count() {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Long> query = builder.createQuery(Long.class);
			query.select(builder.count(query.from(Student.class)));
			return session.createQuery(query).getSingleResult();
		} catch (Exception e) {
			throw new DaoException("Could not get amount of students", e);
		}
	}

	@Override
	public Page<Student> getAllPage(Pageable pageable) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaQuery<Student> query = session.getCriteriaBuilder().createQuery(Student.class);
			query.select(query.from(Student.class));
			List<Student> students = session.createQuery(query)
					.setFirstResult((int) pageable.getOffset())
					.setMaxResults(pageable.getPageSize())
					.getResultList();
			return new PageImpl<>(students, pageable, count());
		} catch (Exception e) {
			throw new DaoException("Could not get students", e);
		}
	}

	@Override
	public List<Student> getByGroup(Group group) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Student> query = builder.createQuery(Student.class);
			Root<Student> root = query.from(Student.class);
			query.select(root).where(builder.equal(root.get("group"), group));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get students by group: " + group, e);
		}
	}

	@Override
	public List<Student> getByCourseId(Long courseId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Student> query = builder.createQuery(Student.class);
			Root<Student> root = query.from(Student.class);
			query.select(root).where(builder.equal(root.join("courses").get("id"), courseId));
			return session.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new DaoException("Could not get students by course id: " + courseId, e);
		}
	}
}
