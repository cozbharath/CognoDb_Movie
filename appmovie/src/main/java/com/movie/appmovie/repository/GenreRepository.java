package com.movie.appmovie.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

import com.movie.appmovie.entity.Genre;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GenreRepository {

	private final Driver driver;

	// CREATE GENRE
	public Genre save(Genre genre) {

		String genreId = UUID.randomUUID().toString();

		genre.setGenreId(genreId);

		String query = """
				CREATE (g:Genre {
				    genreId: $genreId,
				    genreName: $genreName,
				    description: $description
				})
				RETURN g
				""";

		try (Session session = driver.session()) {

			Record record = session.run(query, getParameters(genre)).single();

			return mapToGenre(record);
		}
	}

	// GET ALL GENRES
	public List<Genre> findAll() {

		String query = """
				MATCH (g:Genre)
				RETURN g
				ORDER BY g.genreName
				""";

		try (Session session = driver.session()) {

			return session.run(query).list(this::mapToGenre);
		}
	}

	// GET GENRE BY ID
	public Genre findById(String genreId) {

		String query = """
				MATCH (g:Genre {genreId: $genreId})
				RETURN g
				""";

		try (Session session = driver.session()) {

			List<Genre> genres = session.run(query, Map.of("genreId", genreId)).list(this::mapToGenre);

			if (genres.isEmpty()) {
				return null;
			}

			return genres.get(0);
		}
	}

	// UPDATE GENRE
	public Genre update(Genre genre) {

		String query = """
				MATCH (g:Genre {genreId: $genreId})
				SET g.genreName = $genreName,
				    g.description = $description
				RETURN g
				""";

		try (Session session = driver.session()) {

			List<Genre> genres = session.run(query, getParameters(genre)).list(this::mapToGenre);

			if (genres.isEmpty()) {
				return null;
			}

			return genres.get(0);
		}
	}

	// DELETE GENRE
	public void deleteById(String genreId) {

		String query = """
				MATCH (g:Genre {genreId: $genreId})
				DETACH DELETE g
				""";

		try (Session session = driver.session()) {

			session.run(query, Map.of("genreId", genreId)).consume();
		}
	}

	// PARAMETERS
	private Map<String, Object> getParameters(Genre genre) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put("genreId", genre.getGenreId());
		parameters.put("genreName", genre.getGenreName());
		parameters.put("description", genre.getDescription());

		return parameters;
	}

	// NEO4J NODE → GENRE
	private Genre mapToGenre(Record record) {

		var node = record.get("g").asNode();

		Genre genre = new Genre();

		genre.setGenreId(node.get("genreId").asString());

		genre.setGenreName(node.get("genreName").asString());

		genre.setDescription(node.get("description").asString());

		return genre;
	}
}