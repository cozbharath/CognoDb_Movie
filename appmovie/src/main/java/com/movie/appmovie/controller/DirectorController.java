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

import com.movie.appmovie.request.DirectorRequest;
import com.movie.appmovie.response.DirectorResponse;
import com.movie.appmovie.service.DirectorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/directors")
@RequiredArgsConstructor
public class DirectorController {

	private final DirectorService directorService;

	// CREATE
	@PostMapping
	public ResponseEntity<DirectorResponse> createDirector(@RequestBody DirectorRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(directorService.createDirector(request));
	}

	// GET ALL
	@GetMapping
	public ResponseEntity<List<DirectorResponse>> getAllDirectors() {

		return ResponseEntity.ok(directorService.getAllDirectors());
	}

	// GET BY ID
	@GetMapping("/{id}")
	public ResponseEntity<DirectorResponse> getDirectorById(@PathVariable String id) {

		return ResponseEntity.ok(directorService.getDirectorById(id));
	}

	// UPDATE
	@PutMapping("/{id}")
	public ResponseEntity<DirectorResponse> updateDirector(@PathVariable String id,
			@RequestBody DirectorRequest request) {

		return ResponseEntity.ok(directorService.updateDirector(id, request));
	}

	// DELETE
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDirector(@PathVariable String id) {

		directorService.deleteDirector(id);

		return ResponseEntity.noContent().build();
	}
}