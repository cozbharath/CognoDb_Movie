package com.movie.appmovie.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorRequest {

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

	private String gender;

	private String nationality;

}
