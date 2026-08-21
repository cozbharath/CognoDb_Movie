package com.movie.appmovie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.appmovie.entity.User;
import com.movie.appmovie.mapper.UserMapper;
import com.movie.appmovie.repository.MovieRepository;
import com.movie.appmovie.repository.UserRepository;
import com.movie.appmovie.request.UserRequest;
import com.movie.appmovie.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final MovieRepository movieRepository;

	// =========================================================
	// CREATE USER
	// =========================================================

	public UserResponse createUser(UserRequest request) {

		User existingUser = userRepository.findByEmail(request.getEmail());

		if (existingUser != null) {

			throw new RuntimeException("Email already registered: " + request.getEmail());
		}

		User user = UserMapper.toEntity(request);

		User savedUser = userRepository.save(user);

		return UserMapper.toResponse(savedUser);
	}

	// =========================================================
	// GET ALL USERS
	// =========================================================

	public List<UserResponse> getAllUsers() {

		return userRepository.findAll().stream().map(UserMapper::toResponse).toList();
	}

	// =========================================================
	// GET USER BY ID
	// =========================================================

	public UserResponse getUserById(String id) {

		User user = userRepository.findById(id);

		if (user == null) {

			throw new RuntimeException("User not found with id: " + id);
		}

		return UserMapper.toResponse(user);
	}

	// =========================================================
	// UPDATE USER
	// =========================================================

	public UserResponse updateUser(String id, UserRequest request) {

		User user = userRepository.findById(id);

		if (user == null) {

			throw new RuntimeException("User not found with id: " + id);
		}

		user.setFirstName(request.getFirstName());

		user.setLastName(request.getLastName());

		user.setEmail(request.getEmail());

		if (request.getPassword() != null && !request.getPassword().isBlank()) {

			user.setPasswordHash(request.getPassword());
		}

		user.setDateOfBirth(request.getDateOfBirth());

		user.setCountry(request.getCountry());

		User updatedUser = userRepository.update(user);

		return UserMapper.toResponse(updatedUser);
	}

	// =========================================================
	// DELETE USER
	// =========================================================

	public void deleteUser(String id) {

		User user = userRepository.findById(id);

		if (user == null) {

			throw new RuntimeException("User not found with id: " + id);
		}

		userRepository.deleteById(id);
	}

	// =========================================================
	// MARK MOVIE AS WATCHED
	// =========================================================

	public void markMovieAsWatched(String userId, String movieId) {

		User user = userRepository.findById(userId);

		if (user == null) {

			throw new RuntimeException("User not found with id: " + userId);
		}

		if (movieRepository.findById(movieId) == null) {

			throw new RuntimeException("Movie not found with id: " + movieId);
		}

		userRepository.addWatchedMovie(userId, movieId);
	}

	// =========================================================
	// REMOVE WATCHED MOVIE
	// =========================================================

	public void removeWatchedMovie(String userId, String movieId) {

		User user = userRepository.findById(userId);

		if (user == null) {

			throw new RuntimeException("User not found with id: " + userId);
		}

		if (movieRepository.findById(movieId) == null) {

			throw new RuntimeException("Movie not found with id: " + movieId);
		}

		userRepository.removeWatchedMovie(userId, movieId);
	}
}