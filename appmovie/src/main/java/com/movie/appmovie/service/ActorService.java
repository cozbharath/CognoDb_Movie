package com.movie.appmovie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.appmovie.entity.Actor;
import com.movie.appmovie.mapper.ActorMapper;
import com.movie.appmovie.repository.ActorRepository;
import com.movie.appmovie.request.ActorRequest;
import com.movie.appmovie.response.ActorResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActorService {

	private final ActorRepository actorRepository;

	// CREATE
	public ActorResponse createActor(ActorRequest request) {

		Actor actor = ActorMapper.toEntity(request);

		Actor savedActor = actorRepository.save(actor);

		return ActorMapper.toResponse(savedActor);
	}

	// GET ALL
	public List<ActorResponse> getAllActors() {

		return actorRepository.findAll().stream().map(ActorMapper::toResponse).toList();
	}

	// GET BY ID
	public ActorResponse getActorById(String id) {

		Actor actor = actorRepository.findById(id);

		if (actor == null) {
			throw new RuntimeException("Actor not found with id: " + id);
		}

		return ActorMapper.toResponse(actor);
	}

	// UPDATE
	public ActorResponse updateActor(String id, ActorRequest request) {

		Actor actor = actorRepository.findById(id);

		if (actor == null) {
			throw new RuntimeException("Actor not found with id: " + id);
		}

		actor.setFirstName(request.getFirstName());
		actor.setLastName(request.getLastName());
		actor.setDateOfBirth(request.getDateOfBirth());
		actor.setGender(request.getGender());
		actor.setNationality(request.getNationality());

		Actor updatedActor = actorRepository.update(actor);

		return ActorMapper.toResponse(updatedActor);
	}

	// DELETE
	public void deleteActor(String id) {

		Actor actor = actorRepository.findById(id);

		if (actor == null) {
			throw new RuntimeException("Actor not found with id: " + id);
		}

		actorRepository.deleteById(id);
	}
}