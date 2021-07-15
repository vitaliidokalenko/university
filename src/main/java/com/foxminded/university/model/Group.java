package com.foxminded.university.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "groups")
@Getter
@Setter
@EqualsAndHashCode(of = { "id", "name" })
@ToString(of = { "id", "name" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 10)
	private String name;

	@OneToMany(mappedBy = "group")
	@JsonIgnoreProperties({ "courses", "group" })
	private List<Student> students;
}
