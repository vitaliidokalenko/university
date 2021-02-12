package com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

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
	public void givenNameIsNull_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Group group = buildGroup();
		group.setName(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> groupService.create(group));
		assertEquals("Empty group name", exception.getMessage());
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenIllegalFieldEntityExceptionThrown() {
		Group group = buildGroup();
		group.setName("");

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> groupService.create(group));
		assertEquals("Empty group name", exception.getMessage());
	}

	@Test
	public void givenId_whenFindById_thenGetRightGroupWithItStudents() {
		Optional<Group> expected = Optional.of(buildGroup());
		List<Student> students = Arrays.asList(new Student("Avraam", "Melburn"), new Student("Homer", "Mahony"));
		when(groupDao.findById(1L)).thenReturn(expected);
		when(studentDao.getByGroup(expected.get())).thenReturn(students);

		Optional<Group> actual = groupService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfGroups() {
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
	public void givenEntityIsNotPresent_whenDeleteById_thenNotFoundEntityExceptionThrown() {
		when(groupDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> groupService.deleteById(1L));
		assertEquals("Cannot find group by id: 1", exception.getMessage());
	}

	@Test
	public void givenNameIsNotUnique_whenCreate_thenNotUniqueNameExceptionThrown() {
		Group actual = buildGroup();
		Group retrieved = buildGroup();
		retrieved.setId(2L);
		when(groupDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		Exception exception = assertThrows(NotUniqueNameException.class, () -> groupService.create(actual));
		assertEquals(format("The group with name %s already exists", actual.getName()), exception.getMessage());
	}

	@Test
	public void givenNameIsUnique_whenCreate_thenGroupIsCreating() {
		Group actual = buildGroup();
		when(groupDao.findByName(actual.getName())).thenReturn(Optional.empty());

		groupService.create(actual);

		verify(groupDao).create(actual);
	}

	@Test
	public void givenNameIsNotUnique_whenUpdate_thenNotUniqueNameExceptionThrown() {
		Group actual = buildGroup();
		Group retrieved = buildGroup();
		retrieved.setId(2L);
		when(groupDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		Exception exception = assertThrows(NotUniqueNameException.class, () -> groupService.update(actual));
		assertEquals(format("The group with name %s already exists", actual.getName()), exception.getMessage());
	}

	@Test
	public void givenNameIsUnique_whenUpdate_thenGroupIsUpdating() {
		Group actual = buildGroup();
		Group retrieved = buildGroup();
		when(groupDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		groupService.update(actual);

		verify(groupDao).update(actual);
	}

	private Group buildGroup() {
		Group group = new Group("AA-11");
		group.setId(1L);
		return group;
	}
}
