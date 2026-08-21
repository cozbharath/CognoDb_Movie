package com.movie.appmovie.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectorRequest {

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

	private String nationality;
}