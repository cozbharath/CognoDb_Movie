package com.movie.appmovie.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {

	private String movieId;

	private String title;

	private LocalDate releaseDate;

	private Integer durationMinutes;

	private String language;

	private String country;

	private String description;

	private Double rating;

	private Set<String> actorIds;

	private Set<String> directorIds;

	private Set<String> genreIds;
}