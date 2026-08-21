package com.movie.appmovie.mapper;

import com.movie.appmovie.entity.Actor;
import com.movie.appmovie.request.ActorRequest;
import com.movie.appmovie.response.ActorResponse;

public class ActorMapper {

	private ActorMapper() {
	}

	// Request → Entity
	public static Actor toEntity(ActorRequest request) {

		Actor actor = new Actor();

		actor.setFirstName(request.getFirstName());
		actor.setLastName(request.getLastName());
		actor.setDateOfBirth(request.getDateOfBirth());
		actor.setGender(request.getGender());
		actor.setNationality(request.getNationality());

		return actor;
	}

	// Entity → Response
	public static ActorResponse toResponse(Actor actor) {

		ActorResponse response = new ActorResponse();

		response.setActorId(actor.getActorId());
		response.setFirstName(actor.getFirstName());
		response.setLastName(actor.getLastName());
		response.setDateOfBirth(actor.getDateOfBirth());
		response.setGender(actor.getGender());
		response.setNationality(actor.getNationality());

		return response;
	}
}