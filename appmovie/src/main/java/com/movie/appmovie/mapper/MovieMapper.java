package com.movie.appmovie.mapper;

import java.util.HashSet;

import com.movie.appmovie.entity.Movie;
import com.movie.appmovie.request.MovieRequest;
import com.movie.appmovie.response.MovieResponse;

public class MovieMapper {

	private MovieMapper() {
	}

	// Request → Entity
	public static Movie toEntity(MovieRequest request) {

		Movie movie = new Movie();

		movie.setTitle(request.getTitle());
		movie.setReleaseDate(request.getReleaseDate());
		movie.setDurationMinutes(request.getDurationMinutes());
		movie.setLanguage(request.getLanguage());
		movie.setCountry(request.getCountry());
		movie.setDescription(request.getDescription());
		movie.setRating(request.getRating());

		if (request.getActorIds() != null) {
			movie.setActorIds(new HashSet<>(request.getActorIds()));
		}

		if (request.getDirectorIds() != null) {
			movie.setDirectorIds(new HashSet<>(request.getDirectorIds()));
		}

		if (request.getGenreIds() != null) {
			movie.setGenreIds(new HashSet<>(request.getGenreIds()));
		}

		return movie;
	}

	// Entity → Response
	public static MovieResponse toResponse(Movie movie) {

		MovieResponse response = new MovieResponse();

		response.setMovieId(movie.getMovieId());
		response.setTitle(movie.getTitle());
		response.setReleaseDate(movie.getReleaseDate());
		response.setDurationMinutes(movie.getDurationMinutes());
		response.setLanguage(movie.getLanguage());
		response.setCountry(movie.getCountry());
		response.setDescription(movie.getDescription());
		response.setRating(movie.getRating());

		response.setActorIds(movie.getActorIds());
		response.setDirectorIds(movie.getDirectorIds());
		response.setGenreIds(movie.getGenreIds());

		return response;
	}
}