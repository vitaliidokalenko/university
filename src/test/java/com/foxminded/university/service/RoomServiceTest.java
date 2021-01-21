package com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
		Room room = new Room("111");

		roomService.create(room);

		verify(roomDao).create(room);
	}
	
	@Test
	public void givenId_whenFindById_thenGetRightData() {
		Room expected = new Room("111");
		Long id = 1L;
		when(roomDao.findById(id)).thenReturn(expected);
		
		Room actual = roomService.findById(id);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void whenGetAll_thenGetRightData() {
		List<Room> expected = Arrays.asList(new Room("111"));
		when(roomDao.getAll()).thenReturn(expected);
		
		List<Room> actual = roomService.getAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void givenRoom_whenUpdate_thenGrouRoomIsUpdating() {
		Room room = new Room("111");
		
		roomService.update(room);
		
		verify(roomDao).update(room);
	}
	
	@Test
	public void givenId_whenDeleteById_thenRoomIsDeleting() {
		roomService.deleteById(1L);
		
		verify(roomDao).deleteById(1L);
	}
	
	@Test
	public void givenId_whenGetRoomsByCourseId_thenGetRightData() {
		List<Room> expected = Arrays.asList(new Room("111"));
		when(roomDao.getRoomsByCourseId(1L)).thenReturn(expected);
		
		List<Room> actual = roomService.getRoomsByCourseId(1L);
		
		assertEquals(expected, actual);
	}
}
