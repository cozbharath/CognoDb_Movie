package com.movie.appmovie.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.appmovie.request.UserRequest;
import com.movie.appmovie.response.UserResponse;
import com.movie.appmovie.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// =========================================================
	// CREATE USER
	// =========================================================

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
	}

	// =========================================================
	// GET ALL USERS
	// =========================================================

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {

		return ResponseEntity.ok(userService.getAllUsers());
	}

	// =========================================================
	// GET USER BY ID
	// =========================================================

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {

		return ResponseEntity.ok(userService.getUserById(id));
	}

	// =========================================================
	// UPDATE USER
	// =========================================================

	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UserRequest request) {

		return ResponseEntity.ok(userService.updateUser(id, request));
	}

	// =========================================================
	// DELETE USER
	// =========================================================

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable String id) {

		userService.deleteUser(id);

		return ResponseEntity.noContent().build();
	}

	// =========================================================
	// MARK MOVIE AS WATCHED
	// =========================================================

	@PostMapping("/{userId}/watched/{movieId}")
	public ResponseEntity<String> markMovieAsWatched(@PathVariable String userId, @PathVariable String movieId) {

		userService.markMovieAsWatched(userId, movieId);

		return ResponseEntity.ok("Movie marked as watched");
	}

	// =========================================================
	// REMOVE WATCHED MOVIE
	// =========================================================

	@DeleteMapping("/{userId}/watched/{movieId}")
	public ResponseEntity<String> removeWatchedMovie(@PathVariable String userId, @PathVariable String movieId) {

		userService.removeWatchedMovie(userId, movieId);

		return ResponseEntity.ok("Movie removed from watched list");
	}
}