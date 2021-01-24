package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
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
		Room room = getStandardRoom();

		roomService.create(room);

		verify(roomDao).create(room);
	}

	@Test
	public void givenCapacityLessThanOne_whenCreate_thenRoomIsNotCreating() {
		Room room = getStandardRoom();
		room.setCapacity(0);

		roomService.create(room);

		verify(roomDao, never()).create(room);

	}
	
	@Test
	public void givenNameIsEmpty_whenCreate_thenRoomIsNotCreating() {
		Room room = getStandardRoom();
		room.setName("");

		roomService.create(room);

		verify(roomDao, never()).create(room);

	}
	
	@Test
	public void givenNameIsNull_whenCreate_thenRoomIsNotCreating() {
		Room room = getStandardRoom();
		room.setName(null);

		roomService.create(room);

		verify(roomDao, never()).create(room);

	}

	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Room expected = getStandardRoom();
		when(roomDao.findById(1L)).thenReturn(expected);

		Room actual = roomService.findById(1L);

		assertEquals(expected, actual);
	}

	@Test
	public void whenGetAll_thenGetRightData() {
		List<Room> expected = Arrays.asList(getStandardRoom());
		when(roomDao.getAll()).thenReturn(expected);

		List<Room> actual = roomService.getAll();

		assertEquals(expected, actual);
	}

	@Test
	public void givenRoom_whenUpdate_thenRoomIsUpdating() {
		Room room = getStandardRoom();

		roomService.update(room);

		verify(roomDao).update(room);
	}

	@Test
	public void givenId_whenDeleteById_thenRoomIsDeleting() {
		roomService.deleteById(1L);

		verify(roomDao).deleteById(1L);
	}

	private Room getStandardRoom() {
		Room room = new Room("111");
		room.setId(1L);
		room.setCapacity(30);
		return room;
	}
}
