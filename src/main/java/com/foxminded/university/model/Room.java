package com.foxminded.university.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedQuery(
		name = "getAllRooms",
		query = "from Room r")
@NamedQuery(
		name = "countRooms",
		query = "select count(r) from Room r")
@NamedQuery(
		name = "findRoomByName",
		query = "from Room r where r.name = :name")
@Entity
@Table(name = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private int capacity;

	public Room(String name) {
		this.name = name;
	}
}
