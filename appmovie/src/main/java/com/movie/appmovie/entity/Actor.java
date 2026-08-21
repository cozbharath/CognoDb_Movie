package com.movie.appmovie.entity;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actor {

	private String actorId;

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

	private String gender;

	private String nationality;

}
