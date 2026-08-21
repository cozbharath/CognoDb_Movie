package com.movie.appmovie.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectorResponse {

	private String directorId;

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

	private String nationality;
}