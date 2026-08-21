package com.movie.appmovie.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

import com.movie.appmovie.entity.Movie;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MovieRepository {

	private final Driver driver;

	// =========================================================
	// CREATE MOVIE
	// =========================================================

	public Movie save(Movie movie) {

		String movieId = UUID.randomUUID().toString();

		movie.setMovieId(movieId);

		String query = """
				CREATE (m:Movie {
				    movieId: $movieId,
				    title: $title,
				    releaseDate: $releaseDate,
				    durationMinutes: $durationMinutes,
				    language: $language,
				    country: $country,
				    description: $description,
				    rating: $rating
				})

				WITH m

				// Find actors
				OPTIONAL MATCH (a:Actor)
				WHERE a.actorId IN $actorIds

				WITH m, collect(DISTINCT a) AS actors

				// Find directors
				OPTIONAL MATCH (d:Director)
				WHERE d.directorId IN $directorIds

				WITH m, actors, collect(DISTINCT d) AS directors

				// Find genres
				OPTIONAL MATCH (g:Genre)
				WHERE g.genreId IN $genreIds

				WITH m,
				     actors,
				     directors,
				     collect(DISTINCT g) AS genres

				// Create Actor relationships
				FOREACH (a IN actors |
				    FOREACH (x IN CASE
				        WHEN a IS NULL THEN []
				        ELSE [a]
				    END |
				        MERGE (m)-[:ACTED_BY]->(x)
				    )
				)

				// Create Director relationships
				FOREACH (d IN directors |
				    FOREACH (x IN CASE
				        WHEN d IS NULL THEN []
				        ELSE [d]
				    END |
				        MERGE (m)-[:DIRECTED_BY]->(x)
				    )
				)

				// Create Genre relationships
				FOREACH (g IN genres |
				    FOREACH (x IN CASE
				        WHEN g IS NULL THEN []
				        ELSE [g]
				    END |
				        MERGE (m)-[:HAS_GENRE]->(x)
				    )
				)

				RETURN m
				""";

		try (Session session = driver.session()) {

			Record record = session.run(query, getParameters(movie)).single();

			Movie savedMovie = mapToMovie(record);

			loadRelationships(session, savedMovie);

			return savedMovie;
		}
	}

	// =========================================================
	// GET ALL MOVIES
	// =========================================================

	public List<Movie> findAll() {

		String query = """
				MATCH (m:Movie)
				RETURN m
				ORDER BY m.title
				""";

		try (Session session = driver.session()) {

			List<Movie> movies = session.run(query).list(this::mapToMovie);

			for (Movie movie : movies) {
				loadRelationships(session, movie);
			}

			return movies;
		}
	}

	// =========================================================
	// GET MOVIE BY ID
	// =========================================================

	public Movie findById(String movieId) {

		String query = """
				MATCH (m:Movie {
				    movieId: $movieId
				})
				RETURN m
				""";

		try (Session session = driver.session()) {

			List<Movie> movies = session.run(query, Map.of("movieId", movieId)).list(this::mapToMovie);

			if (movies.isEmpty()) {
				return null;
			}

			Movie movie = movies.get(0);

			loadRelationships(session, movie);

			return movie;
		}
	}

	// =========================================================
	// UPDATE MOVIE
	// =========================================================

	public Movie update(Movie movie) {

		String query = """
				MATCH (m:Movie {
				    movieId: $movieId
				})

				SET m.title = $title,
				    m.releaseDate = $releaseDate,
				    m.durationMinutes = $durationMinutes,
				    m.language = $language,
				    m.country = $country,
				    m.description = $description,
				    m.rating = $rating

				// Remove old Actor relationships
				WITH m
				OPTIONAL MATCH (m)-[actorRelation:ACTED_BY]->()
				DELETE actorRelation

				// Remove old Director relationships
				WITH m
				OPTIONAL MATCH (m)-[directorRelation:DIRECTED_BY]->()
				DELETE directorRelation

				// Remove old Genre relationships
				WITH m
				OPTIONAL MATCH (m)-[genreRelation:HAS_GENRE]->()
				DELETE genreRelation

				// Find Actors
				WITH m
				OPTIONAL MATCH (a:Actor)
				WHERE a.actorId IN $actorIds

				WITH m, collect(DISTINCT a) AS actors

				// Find Directors
				OPTIONAL MATCH (d:Director)
				WHERE d.directorId IN $directorIds

				WITH m,
				     actors,
				     collect(DISTINCT d) AS directors

				// Find Genres
				OPTIONAL MATCH (g:Genre)
				WHERE g.genreId IN $genreIds

				WITH m,
				     actors,
				     directors,
				     collect(DISTINCT g) AS genres

				// Create Actor relationships
				FOREACH (a IN actors |
				    FOREACH (x IN CASE
				        WHEN a IS NULL THEN []
				        ELSE [a]
				    END |
				        MERGE (m)-[:ACTED_BY]->(x)
				    )
				)

				// Create Director relationships
				FOREACH (d IN directors |
				    FOREACH (x IN CASE
				        WHEN d IS NULL THEN []
				        ELSE [d]
				    END |
				        MERGE (m)-[:DIRECTED_BY]->(x)
				    )
				)

				// Create Genre relationships
				FOREACH (g IN genres |
				    FOREACH (x IN CASE
				        WHEN g IS NULL THEN []
				        ELSE [g]
				    END |
				        MERGE (m)-[:HAS_GENRE]->(x)
				    )
				)

				RETURN m
				""";

		try (Session session = driver.session()) {

			List<Movie> movies = session.run(query, getParameters(movie)).list(this::mapToMovie);

			if (movies.isEmpty()) {
				return null;
			}

			Movie updatedMovie = movies.get(0);

			loadRelationships(session, updatedMovie);

			return updatedMovie;
		}
	}

	// =========================================================
	// DELETE MOVIE
	// =========================================================

	public void deleteById(String movieId) {

		String query = """
				MATCH (m:Movie {
				    movieId: $movieId
				})

				DETACH DELETE m
				""";

		try (Session session = driver.session()) {

			session.run(query, Map.of("movieId", movieId)).consume();
		}
	}

	// =========================================================
	// GET PARAMETERS
	// =========================================================

	private Map<String, Object> getParameters(Movie movie) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put("movieId", movie.getMovieId());

		parameters.put("title", movie.getTitle());

		parameters.put("releaseDate", movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : null);

		parameters.put("durationMinutes", movie.getDurationMinutes());

		parameters.put("language", movie.getLanguage());

		parameters.put("country", movie.getCountry());

		parameters.put("description", movie.getDescription());

		parameters.put("rating", movie.getRating());

		parameters.put("actorIds", movie.getActorIds() != null ? List.copyOf(movie.getActorIds()) : List.of());

		parameters.put("directorIds", movie.getDirectorIds() != null ? List.copyOf(movie.getDirectorIds()) : List.of());

		parameters.put("genreIds", movie.getGenreIds() != null ? List.copyOf(movie.getGenreIds()) : List.of());

		return parameters;
	}

	// =========================================================
	// NEO4J NODE → MOVIE OBJECT
	// =========================================================

	private Movie mapToMovie(Record record) {

		var node = record.get("m").asNode();

		Movie movie = new Movie();

		// Movie ID
		if (!node.get("movieId").isNull()) {

			movie.setMovieId(node.get("movieId").asString());
		}

		// Title
		if (!node.get("title").isNull()) {

			movie.setTitle(node.get("title").asString());
		}

		// Release Date
		if (!node.get("releaseDate").isNull()) {

			movie.setReleaseDate(java.time.LocalDate.parse(node.get("releaseDate").asString()));
		}

		// Duration
		if (!node.get("durationMinutes").isNull()) {

			movie.setDurationMinutes(node.get("durationMinutes").asInt());
		}

		// Language
		if (!node.get("language").isNull()) {

			movie.setLanguage(node.get("language").asString());
		}

		// Country
		if (!node.get("country").isNull()) {

			movie.setCountry(node.get("country").asString());
		}

		// Description
		if (!node.get("description").isNull()) {

			movie.setDescription(node.get("description").asString());
		}

		// Rating
		if (!node.get("rating").isNull()) {

			movie.setRating(node.get("rating").asDouble());
		}

		// Initialize relationships
		movie.setActorIds(new HashSet<>());

		movie.setDirectorIds(new HashSet<>());

		movie.setGenreIds(new HashSet<>());

		return movie;
	}

	// =========================================================
	// LOAD ACTOR / DIRECTOR / GENRE RELATIONSHIPS
	// =========================================================

	private void loadRelationships(Session session, Movie movie) {

		String query = """
				MATCH (m:Movie {
				    movieId: $movieId
				})

				OPTIONAL MATCH (m)-[:ACTED_BY]->(a:Actor)

				WITH m,
				     collect(DISTINCT a.actorId) AS actorIds

				OPTIONAL MATCH (m)-[:DIRECTED_BY]->(d:Director)

				WITH m,
				     actorIds,
				     collect(DISTINCT d.directorId) AS directorIds

				OPTIONAL MATCH (m)-[:HAS_GENRE]->(g:Genre)

				RETURN
				    actorIds,
				    directorIds,
				    collect(DISTINCT g.genreId) AS genreIds
				""";

		Record record = session.run(query, Map.of("movieId", movie.getMovieId())).single();

		// =====================================================
		// ACTOR IDs
		// =====================================================

		movie.setActorIds(new HashSet<>(record.get("actorIds").asList(value -> value.asString())));

		// =====================================================
		// DIRECTOR IDs
		// =====================================================

		movie.setDirectorIds(new HashSet<>(record.get("directorIds").asList(value -> value.asString())));

		// =====================================================
		// GENRE IDs
		// =====================================================

		movie.setGenreIds(new HashSet<>(record.get("genreIds").asList(value -> value.asString())));
	}
}