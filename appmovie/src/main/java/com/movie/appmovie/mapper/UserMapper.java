package com.movie.appmovie.mapper;

import com.movie.appmovie.entity.User;
import com.movie.appmovie.request.UserRequest;
import com.movie.appmovie.response.UserResponse;

public class UserMapper {

	private UserMapper() {
	}

	// Request → Entity
	public static User toEntity(UserRequest request) {

		User user = new User();

		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPasswordHash(request.getPassword());
		user.setDateOfBirth(request.getDateOfBirth());
		user.setCountry(request.getCountry());

		return user;
	}

	// Entity → Response
	public static UserResponse toResponse(User user) {

		UserResponse response = new UserResponse();

		response.setUserId(user.getUserId());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setEmail(user.getEmail());
		response.setDateOfBirth(user.getDateOfBirth());
		response.setCountry(user.getCountry());
		response.setCreatedAt(user.getCreatedAt());
		response.setWatchedMovieIds(user.getWatchedMovieIds());

		return response;
	}
}