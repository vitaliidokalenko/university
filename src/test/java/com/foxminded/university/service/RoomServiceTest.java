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
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;

@SpringJUnitConfig(TestAppConfig.class)
@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

	@Mock
	private RoomDao roomDao;

	@InjectMocks
	private RoomService roomService;

	@Test
	public void givenRoom_whenCreate_thenRoomIsCreating() {
		Room room = buildRoom();

		roomService.create(room);

		verify(roomDao).create(room);
	}

	@Test
	public void givenCapacityLessThanOne_whenCreate_thenRoomIsNotCreating() {
		Room room = buildRoom();
		room.setCapacity(0);

		roomService.create(room);

		verify(roomDao, never()).create(room);
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenRoomIsNotCreating() {
		Room room = buildRoom();
		room.setName("");

		roomService.create(room);

		verify(roomDao, never()).create(room);
	}

	@Test
	public void givenNameIsNull_whenCreate_thenRoomIsNotCreating() {
		Room room = buildRoom();
		room.setName(null);

		roomService.create(room);

		verify(roomDao, never()).create(room);
	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Optional<Room> expected = Optional.of(buildRoom());
		when(roomDao.findById(1L)).thenReturn(expected);

		Optional<Room> actual = roomService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Room> expected = Arrays.asList(buildRoom());
		when(roomDao.getAll()).thenReturn(expected);

		List<Room> actual = roomService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenRoom_whenUpdate_thenRoomIsUpdating() {
		Room room = buildRoom();

		roomService.update(room);

		verify(roomDao).update(room);
	}

	@Test
	public void givenEntityIsPresent_whenDeleteById_thenRoomIsDeleting() {
		when(roomDao.findById(1L)).thenReturn(Optional.of(buildRoom()));

		roomService.deleteById(1L);

		verify(roomDao).deleteById(1L);
	}

	@Test
	public void givenEntityIsNotPresent_whenDeleteById_thenRoomIsNotDeleting() {
		when(roomDao.findById(1L)).thenReturn(Optional.empty());

		roomService.deleteById(1L);

		verify(roomDao, never()).deleteById(1L);
	}

	@Test
	public void givenNameIsNotUnique_whenCreate_thenRoomIsNotCreating() {
		Room room = buildRoom();
		Room retrieved = buildRoom();
		retrieved.setId(2L);
		when(roomDao.findByName(room.getName())).thenReturn(Optional.of(retrieved));

		roomService.create(room);

		verify(roomDao, never()).create(room);
	}

	private Room buildRoom() {
		Room room = new Room("111");
		room.setId(1L);
		room.setCapacity(30);
		return room;
	}
}
