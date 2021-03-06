package com.foxminded.university.service;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.foxminded.university.dao.GroupDao;
import com.foxminded.university.model.Group;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

	@Mock
	private GroupDao groupDao;

	@InjectMocks
	private GroupService groupService;

	@Test
	public void givenGroup_whenCreate_thenGroupIsCreating() {
		Group group = buildGroup();

		groupService.create(group);

		verify(groupDao).save(group);
	}

	@Test
	public void givenId_whenFindById_thenGetRightGroup() {
		Group expected = buildGroup();
		when(groupDao.findById(1L)).thenReturn(Optional.of(expected));

		Group actual = groupService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfGroups() {
		List<Group> expected = List.of(buildGroup());
		when(groupDao.findAll()).thenReturn(expected);

		List<Group> actual = groupService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenGroup_whenUpdate_thenGroupIsUpdating() {
		Group group = buildGroup();
		when(groupDao.findById(group.getId())).thenReturn(Optional.of(group));

		groupService.update(group);

		verify(groupDao).save(group);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenGroupIsDeleting() {
		Group group = buildGroup();
		when(groupDao.findById(1L)).thenReturn(Optional.of(group));

		groupService.deleteById(1L);

		verify(groupDao).delete(group);
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

		verify(groupDao).save(actual);
	}

	@Test
	public void givenNameIsNotUnique_whenUpdate_thenNotUniqueNameExceptionThrown() {
		Group actual = buildGroup();
		Group retrieved = buildGroup();
		retrieved.setId(2L);
		when(groupDao.findById(actual.getId())).thenReturn(Optional.of(actual));
		when(groupDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		Exception exception = assertThrows(NotUniqueNameException.class, () -> groupService.update(actual));
		assertEquals(format("The group with name %s already exists", actual.getName()), exception.getMessage());
	}

	@Test
	public void givenNameIsUnique_whenUpdate_thenGroupIsUpdating() {
		Group actual = buildGroup();
		Group retrieved = buildGroup();
		when(groupDao.findById(actual.getId())).thenReturn(Optional.of(actual));
		when(groupDao.findByName(actual.getName())).thenReturn(Optional.of(retrieved));

		groupService.update(actual);

		verify(groupDao).save(actual);
	}

	@Test
	public void whenGetAllPage_thenGetRightGroups() {
		Page<Group> expected = new PageImpl<>(List.of(buildGroup()));
		when(groupDao.findAll(PageRequest.of(0, 1))).thenReturn(expected);

		Page<Group> actual = groupService.getAllPage(PageRequest.of(0, 1));

		assertEquals(expected, actual);
	}

	@Test
	public void givenEntityIsNotPresent_whenFindById_thenNotFoundEntityExceptionThrown() {
		when(groupDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> groupService.findById(1L));
		assertEquals("Cannot find group by id: 1", exception.getMessage());
	}

	@Test
	public void givenEntityIsNotPresent_whenUpdate_thenNotFoundEntityExceptionThrown() {
		Group group = buildGroup();
		when(groupDao.findById(group.getId())).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> groupService.update(group));
		assertEquals("Cannot find group by id: 1", exception.getMessage());
	}

	private Group buildGroup() {
		return Group.builder().id(1L).name("AA-11").build();
	}
}
