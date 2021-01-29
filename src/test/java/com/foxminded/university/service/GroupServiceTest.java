package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
		Group group = buildGroup();

		groupService.create(group);

		verify(groupDao).create(group);
	}

	@Test
	public void givenNameIsNull_whenCreate_thenGroupIsCreating() {
		Group group = buildGroup();
		group.setName(null);

		groupService.create(group);

		verify(groupDao, never()).create(group);
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenGroupIsCreating() {
		Group group = buildGroup();
		group.setName("");

		groupService.create(group);

		verify(groupDao, never()).create(group);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Optional<Group> expected = Optional.of(buildGroup());
		List<Student> students = Arrays.asList(new Student("Avraam", "Melburn"), new Student("Homer", "Mahony"));
		when(groupDao.findById(1L)).thenReturn(expected);
		when(studentDao.getStudentsByGroup(expected.get())).thenReturn(students);

		Optional<Group> actual = groupService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Group> expected = Arrays.asList(buildGroup());
		when(groupDao.getAll()).thenReturn(expected);

		List<Group> actual = groupService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroup_whenUpdate_thenGroupIsUpdating() {
		Group group = buildGroup();

		groupService.update(group);

		verify(groupDao).update(group);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenGroupIsDeleting() {
		when(groupDao.findById(1L)).thenReturn(Optional.of(buildGroup()));

		groupService.deleteById(1L);

		verify(groupDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenGroupIsNotDeleting() {
		when(groupDao.findById(1L)).thenReturn(Optional.empty());

		groupService.deleteById(1L);

		verify(groupDao, never()).deleteById(1L);
	}

	@Test
	public void givenNameIsNotUnique_whenCreate_thenGroupIsCreating() {
		Group actual = buildGroup();
		Group retrieved = buildGroup();
		retrieved.setId(2L);
		when(groupDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		groupService.create(actual);

		verify(groupDao, never()).create(actual);
	}

	private Group buildGroup() {
		Group group = new Group("AA-11");
		group.setId(1L);
		return group;
	}
}
