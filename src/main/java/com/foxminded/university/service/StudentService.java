package com.foxminded.university.service;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Course;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@Service
public class StudentService {

	private StudentDao studentDao;
	private GroupDao groupDao;
	private CourseDao courseDao;

	public StudentService(StudentDao studentDao, GroupDao groupDao, CourseDao courseDao) {
		this.studentDao = studentDao;
		this.groupDao = groupDao;
		this.courseDao = courseDao;
	}

	@Transactional
	public void create(Student student) {
		studentDao.create(student);
	}

	@Transactional
	public Student findById(Long id) {
		Student student = studentDao.findById(id);
		student.setCourses(courseDao.getCoursesByStudentId(id).stream().collect(toSet()));
		return student;
	}

	@Transactional
	public List<Student> getAll() {
		return studentDao.getAll();
	}

	@Transactional
	public void update(Student student) {
		studentDao.update(student);
	}

	@Transactional
	public void deleteById(Long id) {
		studentDao.deleteById(id);
	}

	@Transactional
	public void setGroupById(Long studentId, Long groupId) {
		Student student = studentDao.findById(studentId);
		student.setGroup(groupDao.findById(groupId));
		studentDao.update(student);

	}

	@Transactional
	public void removeGroupById(Long studentId) {
		Student student = studentDao.findById(studentId);
		student.setGroup(null);
		studentDao.update(student);
	}

	@Transactional
	public void addCourseById(Long studentId, Long courseId) {
		Student student = studentDao.findById(studentId);
		Set<Course> courses = courseDao.getCoursesByStudentId(studentId).stream().collect(toSet());
		courses.add(courseDao.findById(courseId));
		student.setCourses(courses);
		studentDao.update(student);
	}

	@Transactional
	public void removeCourseById(Long studentId, Long courseId) {
		Student student = studentDao.findById(studentId);
		Set<Course> courses = courseDao.getCoursesByStudentId(studentId).stream().collect(toSet());
		courses.remove(courseDao.findById(courseId));
		student.setCourses(courses);
		studentDao.update(student);
	}

	@Transactional
	public List<Student> getStudentsByGroup(Group group) {
		return studentDao.getStudentsByGroup(group);
	}

	@Transactional
	public List<Student> getStudentsByCourseId(Long courseId) {
		return studentDao.getStudentsByCourseId(courseId);
	}
}
