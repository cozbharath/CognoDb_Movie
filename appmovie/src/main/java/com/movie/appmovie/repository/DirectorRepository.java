package com.movie.appmovie.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

import com.movie.appmovie.entity.Director;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class DirectorRepository {

	private final Driver driver;

	// CREATE DIRECTOR
	public Director save(Director director) {

		String directorId = UUID.randomUUID().toString();

		director.setDirectorId(directorId);

		String query = """
				CREATE (d:Director {
				    directorId: $directorId,
				    firstName: $firstName,
				    lastName: $lastName,
				    dateOfBirth: $dateOfBirth,
				    nationality: $nationality
				})
				RETURN d
				""";

		try (Session session = driver.session()) {

			Record record = session.run(query, getParameters(director)).single();

			return mapToDirector(record);
		}
	}

	// GET ALL DIRECTORS
	public List<Director> findAll() {

		String query = """
				MATCH (d:Director)
				RETURN d
				ORDER BY d.firstName
				""";

		try (Session session = driver.session()) {

			return session.run(query).list(this::mapToDirector);
		}
	}

	// GET DIRECTOR BY ID
	public Director findById(String directorId) {

		String query = """
				MATCH (d:Director {directorId: $directorId})
				RETURN d
				""";

		try (Session session = driver.session()) {

			List<Director> directors = session.run(query, Map.of("directorId", directorId)).list(this::mapToDirector);

			if (directors.isEmpty()) {
				return null;
			}

			return directors.get(0);
		}
	}

	// UPDATE DIRECTOR
	public Director update(Director director) {

		String query = """
				MATCH (d:Director {directorId: $directorId})
				SET d.firstName = $firstName,
				    d.lastName = $lastName,
				    d.dateOfBirth = $dateOfBirth,
				    d.nationality = $nationality
				RETURN d
				""";

		try (Session session = driver.session()) {

			List<Director> directors = session.run(query, getParameters(director)).list(this::mapToDirector);

			if (directors.isEmpty()) {
				return null;
			}

			return directors.get(0);
		}
	}

	// DELETE DIRECTOR
	public void deleteById(String directorId) {

		String query = """
				MATCH (d:Director {directorId: $directorId})
				DETACH DELETE d
				""";

		try (Session session = driver.session()) {

			session.run(query, Map.of("directorId", directorId)).consume();
		}
	}

	// PARAMETERS
	private Map<String, Object> getParameters(Director director) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put("directorId", director.getDirectorId());
		parameters.put("firstName", director.getFirstName());
		parameters.put("lastName", director.getLastName());

		parameters.put("dateOfBirth", director.getDateOfBirth() != null ? director.getDateOfBirth().toString() : null);

		parameters.put("nationality", director.getNationality());

		return parameters;
	}

	// NEO4J NODE → DIRECTOR
	private Director mapToDirector(Record record) {

		var node = record.get("d").asNode();

		Director director = new Director();

		director.setDirectorId(node.get("directorId").asString());

		director.setFirstName(node.get("firstName").asString());

		director.setLastName(node.get("lastName").asString());

		if (!node.get("dateOfBirth").isNull()) {

			director.setDateOfBirth(java.time.LocalDate.parse(node.get("dateOfBirth").asString()));
		}

		director.setNationality(node.get("nationality").asString());

		return director;
	}
}