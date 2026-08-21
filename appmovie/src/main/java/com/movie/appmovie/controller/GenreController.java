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

import com.movie.appmovie.request.GenreRequest;
import com.movie.appmovie.response.GenreResponse;
import com.movie.appmovie.service.GenreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

	private final GenreService genreService;

	// CREATE
	@PostMapping
	public ResponseEntity<GenreResponse> createGenre(@RequestBody GenreRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(genreService.createGenre(request));
	}

	// GET ALL
	@GetMapping
	public ResponseEntity<List<GenreResponse>> getAllGenres() {

		return ResponseEntity.ok(genreService.getAllGenres());
	}

	// GET BY ID
	@GetMapping("/{id}")
	public ResponseEntity<GenreResponse> getGenreById(@PathVariable String id) {

		return ResponseEntity.ok(genreService.getGenreById(id));
	}

	// UPDATE
	@PutMapping("/{id}")
	public ResponseEntity<GenreResponse> updateGenre(@PathVariable String id, @RequestBody GenreRequest request) {

		return ResponseEntity.ok(genreService.updateGenre(id, request));
	}

	// DELETE
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteGenre(@PathVariable String id) {

		genreService.deleteGenre(id);

		return ResponseEntity.noContent().build();
	}
}