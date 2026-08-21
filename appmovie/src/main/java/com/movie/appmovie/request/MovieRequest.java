package com.movie.appmovie.request;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {

	private String title;

	private LocalDate releaseDate;

	private Integer durationMinutes;

	private String language;

	private String country;

	private String description;

	private Double rating;

	private List<String> actorIds;

	private List<String> directorIds;

	private List<String> genreIds;
}