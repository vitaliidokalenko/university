package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.foxminded.university.config.TestAppConfig;
import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.dao.StudentDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.model.Student;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

	@Mock
	private GroupDao groupDao;
	@Mock
	private StudentDao studentDao;

	@InjectMocks
	private GroupService groupService;

	@Test
	public void givenGroup_whenCreate_thenGroupIsCreating() {
		Group group = new Group("AA-11");

		groupService.create(group);

		verify(groupDao).create(group);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Group expected = new Group("AA-11");
		Long id = 1L;
		expected.setId(id);
		List<Student> students = Arrays.asList(new Student("Avraam", "Melburn"), new Student("Homer", "Mahony"));
		when(groupDao.findById(id)).thenReturn(expected);
		when(studentDao.getStudentsByGroup(expected)).thenReturn(students);

		Group actual = groupService.findById(id);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Group> expected = Arrays.asList(new Group("AA-11"));
		when(groupDao.getAll()).thenReturn(expected);

		List<Group> actual = groupService.getAll();

		assertEquals(expected, actual);

	}

	@Test
	public void givenGroup_whenUpdate_thenGroupIsUpdating() {
		Group group = new Group("AA-11");

		groupService.update(group);

		verify(groupDao).update(group);
	}

	@Test
	public void givenId_whenDeleteById_thenGroupIsDeleting() {
		groupService.deleteById(1L);

		verify(groupDao).deleteById(1L);
	}

	@Test
	public void givenId_whenGetGroupsByLessonId_thenGetRightData() {
		List<Group> expected = Arrays.asList(new Group("AA-11"));
		when(groupDao.getGroupsByLessonId(anyLong())).thenReturn(expected);

		List<Group> actual = groupService.getGroupsByLessonId(anyLong());

		assertEquals(expected, actual);

	}
}
