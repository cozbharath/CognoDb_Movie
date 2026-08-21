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

import com.movie.appmovie.request.MovieRequest;
import com.movie.appmovie.response.MovieResponse;
import com.movie.appmovie.service.MovieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

	private final MovieService movieService;

	// CREATE MOVIE
	@PostMapping
	public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(request));
	}

	// GET ALL MOVIES
	@GetMapping
	public ResponseEntity<List<MovieResponse>> getAllMovies() {

		return ResponseEntity.ok(movieService.getAllMovies());
	}

	// GET MOVIE BY ID
	@GetMapping("/{id}")
	public ResponseEntity<MovieResponse> getMovieById(@PathVariable String id) {

		return ResponseEntity.ok(movieService.getMovieById(id));
	}

	// UPDATE MOVIE
	@PutMapping("/{id}")
	public ResponseEntity<MovieResponse> updateMovie(@PathVariable String id, @RequestBody MovieRequest request) {

		return ResponseEntity.ok(movieService.updateMovie(id, request));
	}

	// DELETE MOVIE
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMovie(@PathVariable String id) {

		movieService.deleteMovie(id);

		return ResponseEntity.noContent().build();
	}
}