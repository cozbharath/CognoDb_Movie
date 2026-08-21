package com.movie.appmovie.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

	private String movieId;

	private String title;

	private LocalDate releaseDate;

	private Integer durationMinutes;

	private String language;

	private String country;

	private String description;

	private Double rating;

	private Set<String> actorIds = new HashSet<>();

	private Set<String> directorIds = new HashSet<>();

	private Set<String> genreIds = new HashSet<>();
}