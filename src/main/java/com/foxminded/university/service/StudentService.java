package com.foxminded.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foxminded.university.dao.CourseDao;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@Service
@PropertySource("application.properties")
public class StudentService {

	private StudentDao studentDao;
	private GroupDao groupDao;
	private CourseDao courseDao;
	@Value("${group.size}")
	private int groupSize;

	public StudentService(StudentDao studentDao, GroupDao groupDao, CourseDao courseDao) {
		this.studentDao = studentDao;
		this.groupDao = groupDao;
		this.courseDao = courseDao;
	}

	@Transactional
	public void create(Student student) {
		if (isStudentValid(student)) {
			studentDao.create(student);
		}
	}

	@Transactional
	public Student findById(Long id) {
		return studentDao.findById(id);
	}

	@Transactional
	public List<Student> getAll() {
		return studentDao.getAll();
	}

	@Transactional
	public void update(Student student) {
		if (isStudentValid(student)) {
			studentDao.update(student);
		}
	}

	@Transactional
	public void deleteById(Long id) {
		studentDao.deleteById(id);
	}

	@Transactional
	public void setGroupById(Long studentId, Long groupId) {
		Student student = studentDao.findById(studentId);
		student.setGroup(groupDao.findById(groupId));
		if (isGroupSizeEnough(student)) {
			studentDao.update(student);
		}
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
		student.getCourses().add(courseDao.findById(courseId));
		studentDao.update(student);
	}

	@Transactional
	public void removeCourseById(Long studentId, Long courseId) {
		Student student = studentDao.findById(studentId);
		student.getCourses().remove(courseDao.findById(courseId));
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

	private boolean isStudentValid(Student student) {
		return student.getGender() != null
				&& student.getName() != null
				&& student.getSurname() != null
				&& !student.getName().isEmpty()
				&& !student.getSurname().isEmpty()
				&& isGroupSizeEnough(student);
	}

	private boolean isGroupSizeEnough(Student student) {
		return studentDao.getStudentsByGroup(student.getGroup())
				.stream()
				.count()
				< groupSize;
	}
}
