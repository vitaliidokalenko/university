package com.foxminded.university.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.foxminded.university.dao.StudentDao;
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
		sessionFactory.getCurrentSession().save(student);
	}

	@Override
	public Optional<Student> findById(Long id) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getStudentById")
				.setParameter("id", id)
				.uniqueResultOptional();
	}

	@Override
	public List<Student> getAll() {
		return sessionFactory.getCurrentSession().getNamedQuery("getAllStudents").getResultList();
	}

	@Override
	public void update(Student student) {
		sessionFactory.getCurrentSession().merge(student);
	}

	@Override
	public void delete(Student student) {
		sessionFactory.getCurrentSession().delete(student);
	}

	@Override
	public long count() {
		return (long) sessionFactory.getCurrentSession().getNamedQuery("countStudents").getSingleResult();
	}

	@Override
	public Page<Student> getAllPage(Pageable pageable) {
		List<Student> students = sessionFactory.getCurrentSession()
				.getNamedQuery("getAllStudents")
				.setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize())
				.getResultList();
		return new PageImpl<>(students, pageable, count());
	}

	@Override
	public List<Student> getByGroup(Group group) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getStudentsByGroup")
				.setParameter("group", group)
				.getResultList();
	}

	@Override
	public List<Student> getByCourseId(Long courseId) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("getStudentsByCourseId")
				.setParameter("id", courseId)
				.getResultList();
	}
}
