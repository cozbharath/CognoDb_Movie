package com.movie.appmovie.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

	private String userId;

	private String firstName;

	private String lastName;

	private String email;

	private LocalDate dateOfBirth;

	private String country;

	private LocalDateTime createdAt;

	private Set<String> watchedMovieIds;
}