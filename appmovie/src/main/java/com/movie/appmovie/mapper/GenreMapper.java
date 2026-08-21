package com.movie.appmovie.mapper;

import com.movie.appmovie.entity.Genre;
import com.movie.appmovie.request.GenreRequest;
import com.movie.appmovie.response.GenreResponse;

public class GenreMapper {

	private GenreMapper() {
	}

	// Request → Entity
	public static Genre toEntity(GenreRequest request) {

		Genre genre = new Genre();

		genre.setGenreName(request.getGenreName());
		genre.setDescription(request.getDescription());

		return genre;
	}

	// Entity → Response
	public static GenreResponse toResponse(Genre genre) {

		GenreResponse response = new GenreResponse();

		response.setGenreId(genre.getGenreId());
		response.setGenreName(genre.getGenreName());
		response.setDescription(genre.getDescription());

		return response;
	}
}