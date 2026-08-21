package com.movie.appmovie.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

import com.movie.appmovie.entity.Actor;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ActorRepository {

	private final Driver driver;

	// CREATE ACTOR
	public Actor save(Actor actor) {

		String actorId = UUID.randomUUID().toString();

		actor.setActorId(actorId);

		String query = """
				CREATE (a:Actor {
				    actorId: $actorId,
				    firstName: $firstName,
				    lastName: $lastName,
				    dateOfBirth: $dateOfBirth,
				    gender: $gender,
				    nationality: $nationality
				})
				RETURN a
				""";

		try (Session session = driver.session()) {

			Record record = session.run(query, getParameters(actor)).single();

			return mapToActor(record);
		}
	}

	// GET ALL ACTORS
	public List<Actor> findAll() {

		String query = """
				MATCH (a:Actor)
				RETURN a
				ORDER BY a.firstName
				""";

		try (Session session = driver.session()) {

			return session.run(query).list(this::mapToActor);
		}
	}

	// GET ACTOR BY ID
	public Actor findById(String actorId) {

		String query = """
				MATCH (a:Actor {actorId: $actorId})
				RETURN a
				""";

		try (Session session = driver.session()) {

			List<Actor> actors = session.run(query, Map.of("actorId", actorId)).list(this::mapToActor);

			if (actors.isEmpty()) {
				return null;
			}

			return actors.get(0);
		}
	}

	// UPDATE ACTOR
	public Actor update(Actor actor) {

		String query = """
				MATCH (a:Actor {actorId: $actorId})
				SET a.firstName = $firstName,
				    a.lastName = $lastName,
				    a.dateOfBirth = $dateOfBirth,
				    a.gender = $gender,
				    a.nationality = $nationality
				RETURN a
				""";

		try (Session session = driver.session()) {

			List<Actor> actors = session.run(query, getParameters(actor)).list(this::mapToActor);

			if (actors.isEmpty()) {
				return null;
			}

			return actors.get(0);
		}
	}

	// DELETE ACTOR
	public void deleteById(String actorId) {

		String query = """
				MATCH (a:Actor {actorId: $actorId})
				DETACH DELETE a
				""";

		try (Session session = driver.session()) {

			session.run(query, Map.of("actorId", actorId)).consume();
		}
	}

	// PARAMETERS
	private Map<String, Object> getParameters(Actor actor) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put("actorId", actor.getActorId());
		parameters.put("firstName", actor.getFirstName());
		parameters.put("lastName", actor.getLastName());

		parameters.put("dateOfBirth", actor.getDateOfBirth() != null ? actor.getDateOfBirth().toString() : null);

		parameters.put("gender", actor.getGender());
		parameters.put("nationality", actor.getNationality());

		return parameters;
	}

	// NEO4J NODE → ACTOR
	private Actor mapToActor(Record record) {

		var node = record.get("a").asNode();

		Actor actor = new Actor();

		actor.setActorId(node.get("actorId").asString());
		actor.setFirstName(node.get("firstName").asString());
		actor.setLastName(node.get("lastName").asString());

		if (!node.get("dateOfBirth").isNull()) {
			actor.setDateOfBirth(java.time.LocalDate.parse(node.get("dateOfBirth").asString()));
		}

		actor.setGender(node.get("gender").asString());
		actor.setNationality(node.get("nationality").asString());

		return actor;
	}
}