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

import com.movie.appmovie.request.ActorRequest;
import com.movie.appmovie.response.ActorResponse;
import com.movie.appmovie.service.ActorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/actors")
@RequiredArgsConstructor
public class ActorController {

	private final ActorService actorService;

	// CREATE ACTOR
	@PostMapping
	public ResponseEntity<ActorResponse> createActor(@RequestBody ActorRequest request) {

		return ResponseEntity.status(HttpStatus.CREATED).body(actorService.createActor(request));
	}

	// GET ALL ACTORS
	@GetMapping
	public ResponseEntity<List<ActorResponse>> getAllActors() {

		return ResponseEntity.ok(actorService.getAllActors());
	}

	// GET ACTOR BY ID
	@GetMapping("/{id}")
	public ResponseEntity<ActorResponse> getActorById(@PathVariable String id) {

		return ResponseEntity.ok(actorService.getActorById(id));
	}

	// UPDATE ACTOR
	@PutMapping("/{id}")
	public ResponseEntity<ActorResponse> updateActor(@PathVariable String id, @RequestBody ActorRequest request) {

		return ResponseEntity.ok(actorService.updateActor(id, request));
	}

	// DELETE ACTOR
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteActor(@PathVariable String id) {

		actorService.deleteActor(id);

		return ResponseEntity.noContent().build();
	}
}