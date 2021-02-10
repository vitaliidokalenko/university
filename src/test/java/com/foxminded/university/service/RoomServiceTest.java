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
import com.foxminded.university.dao.RoomDao;
import com.foxminded.university.model.Room;
import com.foxminded.university.service.exception.IllegalFieldEntityException;
import com.foxminded.university.service.exception.NotFoundEntityException;
import com.foxminded.university.service.exception.NotUniqueNameException;

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
	public void givenCapacityLessThanOne_whenCreate_thenThrowException() {
		Room room = buildRoom();
		room.setCapacity(0);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> roomService.create(room));
		assertEquals(format("Capacity of the room %s is less than 1", room.getName()), exception.getMessage());
	}

	@Test
	public void givenNameIsEmpty_whenCreate_thenThrowException() {
		Room room = buildRoom();
		room.setName("");

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> roomService.create(room));
		assertEquals("The name of the room is absent", exception.getMessage());
	}

	@Test
	public void givenNameIsNull_whenCreate_thenThrowException() {
		Room room = buildRoom();
		room.setName(null);

		Exception exception = assertThrows(IllegalFieldEntityException.class, () -> roomService.create(room));
		assertEquals("The name of the room is absent", exception.getMessage());
	}

	@Test
	public void givenId_whenFindById_thenGetRightRoom() {
		Optional<Room> expected = Optional.of(buildRoom());
		when(roomDao.findById(1L)).thenReturn(expected);

		Optional<Room> actual = roomService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightListOfRooms() {
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
	public void givenEntityIsNotPresent_whenDeleteById_thenThrowException() {
		when(roomDao.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundEntityException.class, () -> roomService.deleteById(1L));
		assertEquals("Cannot find room by id: 1", exception.getMessage());
	}

	@Test
	public void givenNameIsNotUnique_whenCreate_thenThrowException() {
		Room room = buildRoom();
		Room retrieved = buildRoom();
		retrieved.setId(2L);
		when(roomDao.findByName(room.getName())).thenReturn(Optional.of(retrieved));

		Exception exception = assertThrows(NotUniqueNameException.class, () -> roomService.create(room));
		assertEquals(format("The room with name %s already exists", room.getName()), exception.getMessage());
	}

	@Test
	public void givenNameIsUnique_whenCreate_thenRoomIsCreating() {
		Room room = buildRoom();
		when(roomDao.findByName(room.getName())).thenReturn(Optional.empty());

		roomService.create(room);

		verify(roomDao).create(room);
	}

	@Test
	public void givenNameIsNotUnique_whenUpdate_thenThrowException() {
		Room room = buildRoom();
		Room retrieved = buildRoom();
		retrieved.setId(2L);
		when(roomDao.findByName(room.getName())).thenReturn(Optional.of(retrieved));

		Exception exception = assertThrows(NotUniqueNameException.class, () -> roomService.update(room));
		assertEquals(format("The room with name %s already exists", room.getName()), exception.getMessage());
	}

	@Test
	public void givenNameIsUnique_whenUpdate_thenRoomIsUpdating() {
		Room room = buildRoom();
		Room retrieved = buildRoom();
		when(roomDao.findByName(room.getName())).thenReturn(Optional.of(retrieved));

		roomService.update(room);

		verify(roomDao).update(room);
	}

	private Room buildRoom() {
		Room room = new Room("111");
		room.setId(1L);
		room.setCapacity(30);
		return room;
	}
}
