package com.movie.appmovie.mapper;

import com.movie.appmovie.entity.Director;
import com.movie.appmovie.request.DirectorRequest;
import com.movie.appmovie.response.DirectorResponse;

public class DirectorMapper {

	private DirectorMapper() {
	}

	// Request → Entity
	public static Director toEntity(DirectorRequest request) {

		Director director = new Director();

		director.setFirstName(request.getFirstName());
		director.setLastName(request.getLastName());
		director.setDateOfBirth(request.getDateOfBirth());
		director.setNationality(request.getNationality());

		return director;
	}

	// Entity → Response
	public static DirectorResponse toResponse(Director director) {

		DirectorResponse response = new DirectorResponse();

		response.setDirectorId(director.getDirectorId());
		response.setFirstName(director.getFirstName());
		response.setLastName(director.getLastName());
		response.setDateOfBirth(director.getDateOfBirth());
		response.setNationality(director.getNationality());

		return response;
	}
}