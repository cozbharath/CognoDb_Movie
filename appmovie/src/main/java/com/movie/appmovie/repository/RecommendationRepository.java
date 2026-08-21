package com.movie.appmovie.repository;

import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

import com.movie.appmovie.response.RecommendationResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecommendationRepository {

	private final Driver driver;

	// =========================================================
	// RECOMMEND MOVIES
	// =========================================================

	public List<RecommendationResponse> getRecommendations(String userId) {

		String query = """
				MATCH (u:User {userId: $userId})
				      -[:WATCHED]->(watched:Movie)

				OPTIONAL MATCH (watched)-[:HAS_GENRE]->(g:Genre)

				OPTIONAL MATCH (watched)-[:ACTED_BY]->(a:Actor)

				OPTIONAL MATCH (watched)-[:DIRECTED_BY]->(d:Director)

				WITH u,
				     collect(DISTINCT watched.movieId) AS watchedMovieIds,
				     collect(DISTINCT g.genreId) AS genreIds,
				     collect(DISTINCT a.actorId) AS actorIds,
				     collect(DISTINCT d.directorId) AS directorIds

				MATCH (recommended:Movie)

				WHERE NOT recommended.movieId IN watchedMovieIds

				OPTIONAL MATCH (recommended)-[:HAS_GENRE]->(rg:Genre)

				OPTIONAL MATCH (recommended)-[:ACTED_BY]->(ra:Actor)

				OPTIONAL MATCH (recommended)-[:DIRECTED_BY]->(rd:Director)

				WITH recommended,
				     watchedMovieIds,
				     genreIds,
				     actorIds,
				     directorIds,
				     count(DISTINCT CASE
				         WHEN rg.genreId IN genreIds
				         THEN rg.genreId
				     END) AS genreScore,
				     count(DISTINCT CASE
				         WHEN ra.actorId IN actorIds
				         THEN ra.actorId
				     END) AS actorScore,
				     count(DISTINCT CASE
				         WHEN rd.directorId IN directorIds
				         THEN rd.directorId
				     END) AS directorScore

				WITH recommended,
				     genreScore,
				     actorScore,
				     directorScore,
				     (
				         genreScore * 3 +
				         actorScore * 2 +
				         directorScore * 2
				     ) AS totalScore

				WHERE totalScore > 0

				RETURN
				    recommended.movieId AS movieId,
				    recommended.title AS title,
				    recommended.releaseDate AS releaseDate,
				    recommended.durationMinutes AS durationMinutes,
				    recommended.language AS language,
				    recommended.country AS country,
				    recommended.description AS description,
				    recommended.rating AS rating,
				    totalScore AS score

				ORDER BY score DESC, recommended.rating DESC
				""";

		try (Session session = driver.session()) {

			return session.run(query, Map.of("userId", userId)).list(this::mapToRecommendation);
		}
	}

	// =========================================================
	// MAP RECORD -> RESPONSE
	// =========================================================

	private RecommendationResponse mapToRecommendation(Record record) {

		RecommendationResponse response = new RecommendationResponse();

		response.setMovieId(record.get("movieId").asString());

		response.setTitle(record.get("title").asString());

		response.setReleaseDate(record.get("releaseDate").isNull() ? null : record.get("releaseDate").asString());

		response.setDurationMinutes(
				record.get("durationMinutes").isNull() ? null : record.get("durationMinutes").asInt());

		response.setLanguage(record.get("language").isNull() ? null : record.get("language").asString());

		response.setCountry(record.get("country").isNull() ? null : record.get("country").asString());

		response.setDescription(record.get("description").isNull() ? null : record.get("description").asString());

		response.setRating(record.get("rating").isNull() ? null : record.get("rating").asDouble());

		response.setScore(record.get("score").asLong());

		return response;
	}
}