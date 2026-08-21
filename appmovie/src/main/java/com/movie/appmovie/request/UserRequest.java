package com.movie.appmovie.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	private LocalDate dateOfBirth;

	private String country;
}