package com.movie.appmovie.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {

	private String movieId;

	private String title;

	private String releaseDate;

	private Integer durationMinutes;

	private String language;

	private String country;

	private String description;

	private Double rating;

	private Long score;
}