package com.movie.appmovie.repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

import com.movie.appmovie.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {

	private final Driver driver;

	// =========================================================
	// CREATE USER
	// =========================================================

	public User save(User user) {

		String userId = UUID.randomUUID().toString();

		user.setUserId(userId);

		LocalDateTime createdAt = LocalDateTime.now();

		user.setCreatedAt(createdAt);

		String query = """
				CREATE (u:User {
				    userId: $userId,
				    firstName: $firstName,
				    lastName: $lastName,
				    email: $email,
				    passwordHash: $passwordHash,
				    dateOfBirth: $dateOfBirth,
				    country: $country,
				    createdAt: $createdAt
				})
				RETURN u
				""";

		try (Session session = driver.session()) {

			Record record = session.run(query, getParameters(user)).single();

			return mapToUser(record);
		}
	}

	// =========================================================
	// GET ALL USERS
	// =========================================================

	public List<User> findAll() {

		String query = """
				MATCH (u:User)
				RETURN u
				ORDER BY u.firstName
				""";

		try (Session session = driver.session()) {

			List<User> users = session.run(query).list(this::mapToUser);

			for (User user : users) {
				loadWatchedMovies(session, user);
			}

			return users;
		}
	}

	// =========================================================
	// GET USER BY ID
	// =========================================================

	public User findById(String userId) {

		String query = """
				MATCH (u:User {userId: $userId})
				RETURN u
				""";

		try (Session session = driver.session()) {

			List<User> users = session.run(query, Map.of("userId", userId)).list(this::mapToUser);

			if (users.isEmpty()) {
				return null;
			}

			User user = users.get(0);

			loadWatchedMovies(session, user);

			return user;
		}
	}

	// =========================================================
	// GET USER BY EMAIL
	// =========================================================

	public User findByEmail(String email) {

		String query = """
				MATCH (u:User {email: $email})
				RETURN u
				""";

		try (Session session = driver.session()) {

			List<User> users = session.run(query, Map.of("email", email)).list(this::mapToUser);

			if (users.isEmpty()) {
				return null;
			}

			return users.get(0);
		}
	}

	// =========================================================
	// UPDATE USER
	// =========================================================

	public User update(User user) {

		String query = """
				MATCH (u:User {userId: $userId})

				SET u.firstName = $firstName,
				    u.lastName = $lastName,
				    u.email = $email,
				    u.passwordHash = $passwordHash,
				    u.dateOfBirth = $dateOfBirth,
				    u.country = $country

				RETURN u
				""";

		try (Session session = driver.session()) {

			List<User> users = session.run(query, getParameters(user)).list(this::mapToUser);

			if (users.isEmpty()) {
				return null;
			}

			User updatedUser = users.get(0);

			loadWatchedMovies(session, updatedUser);

			return updatedUser;
		}
	}

	// =========================================================
	// DELETE USER
	// =========================================================

	public void deleteById(String userId) {

		String query = """
				MATCH (u:User {userId: $userId})
				DETACH DELETE u
				""";

		try (Session session = driver.session()) {

			session.run(query, Map.of("userId", userId)).consume();
		}
	}

	// =========================================================
	// ADD WATCHED MOVIE
	// =========================================================

	public void addWatchedMovie(String userId, String movieId) {

		String query = """
				MATCH (u:User {userId: $userId})
				MATCH (m:Movie {movieId: $movieId})

				MERGE (u)-[:WATCHED]->(m)
				""";

		try (Session session = driver.session()) {

			session.run(query, Map.of("userId", userId, "movieId", movieId)).consume();
		}
	}

	// =========================================================
	// REMOVE WATCHED MOVIE
	// =========================================================

	public void removeWatchedMovie(String userId, String movieId) {

		String query = """
				MATCH (u:User {userId: $userId})
				      -[r:WATCHED]->
				      (m:Movie {movieId: $movieId})

				DELETE r
				""";

		try (Session session = driver.session()) {

			session.run(query, Map.of("userId", userId, "movieId", movieId)).consume();
		}
	}

	// =========================================================
	// LOAD WATCHED MOVIES
	// =========================================================

	private void loadWatchedMovies(Session session, User user) {

		String query = """
				MATCH (u:User {userId: $userId})

				OPTIONAL MATCH
				    (u)-[:WATCHED]->(m:Movie)

				RETURN collect(
				    DISTINCT m.movieId
				) AS movieIds
				""";

		Record record = session.run(query, Map.of("userId", user.getUserId())).single();

		user.setWatchedMovieIds(new HashSet<>(record.get("movieIds").asList(value -> value.asString())));
	}

	// =========================================================
	// PARAMETERS
	// =========================================================

	private Map<String, Object> getParameters(User user) {

		Map<String, Object> parameters = new HashMap<>();

		parameters.put("userId", user.getUserId());

		parameters.put("firstName", user.getFirstName());

		parameters.put("lastName", user.getLastName());

		parameters.put("email", user.getEmail());

		parameters.put("passwordHash", user.getPasswordHash());

		parameters.put("dateOfBirth", user.getDateOfBirth() != null ? user.getDateOfBirth().toString() : null);

		parameters.put("country", user.getCountry());

		parameters.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);

		return parameters;
	}

	// =========================================================
	// NEO4J NODE → USER
	// =========================================================

	private User mapToUser(Record record) {

		var node = record.get("u").asNode();

		User user = new User();

		if (!node.get("userId").isNull()) {

			user.setUserId(node.get("userId").asString());
		}

		if (!node.get("firstName").isNull()) {

			user.setFirstName(node.get("firstName").asString());
		}

		if (!node.get("lastName").isNull()) {

			user.setLastName(node.get("lastName").asString());
		}

		if (!node.get("email").isNull()) {

			user.setEmail(node.get("email").asString());
		}

		if (!node.get("passwordHash").isNull()) {

			user.setPasswordHash(node.get("passwordHash").asString());
		}

		if (!node.get("dateOfBirth").isNull()) {

			user.setDateOfBirth(java.time.LocalDate.parse(node.get("dateOfBirth").asString()));
		}

		if (!node.get("country").isNull()) {

			user.setCountry(node.get("country").asString());
		}

		if (!node.get("createdAt").isNull()) {

			user.setCreatedAt(LocalDateTime.parse(node.get("createdAt").asString()));
		}

		user.setWatchedMovieIds(new HashSet<>());

		return user;
	}
}