package com.movie.appmovie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.appmovie.entity.Movie;
import com.movie.appmovie.mapper.MovieMapper;
import com.movie.appmovie.repository.ActorRepository;
import com.movie.appmovie.repository.DirectorRepository;
import com.movie.appmovie.repository.GenreRepository;
import com.movie.appmovie.repository.MovieRepository;
import com.movie.appmovie.request.MovieRequest;
import com.movie.appmovie.response.MovieResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

	private final MovieRepository movieRepository;

	private final ActorRepository actorRepository;

	private final DirectorRepository directorRepository;

	private final GenreRepository genreRepository;

	// CREATE MOVIE
	public MovieResponse createMovie(MovieRequest request) {

		validateActors(request.getActorIds());

		validateDirectors(request.getDirectorIds());

		validateGenres(request.getGenreIds());

		Movie movie = MovieMapper.toEntity(request);

		Movie savedMovie = movieRepository.save(movie);

		return MovieMapper.toResponse(savedMovie);
	}

	// GET ALL
	public List<MovieResponse> getAllMovies() {

		return movieRepository.findAll().stream().map(MovieMapper::toResponse).toList();
	}

	// GET BY ID
	public MovieResponse getMovieById(String id) {

		Movie movie = movieRepository.findById(id);

		if (movie == null) {
			throw new RuntimeException("Movie not found with id: " + id);
		}

		return MovieMapper.toResponse(movie);
	}

	// UPDATE
	public MovieResponse updateMovie(String id, MovieRequest request) {

		Movie movie = movieRepository.findById(id);

		if (movie == null) {
			throw new RuntimeException("Movie not found with id: " + id);
		}

		validateActors(request.getActorIds());

		validateDirectors(request.getDirectorIds());

		validateGenres(request.getGenreIds());

		movie.setTitle(request.getTitle());
		movie.setReleaseDate(request.getReleaseDate());
		movie.setDurationMinutes(request.getDurationMinutes());
		movie.setLanguage(request.getLanguage());
		movie.setCountry(request.getCountry());
		movie.setDescription(request.getDescription());
		movie.setRating(request.getRating());

		movie.setActorIds(request.getActorIds() != null ? new java.util.HashSet<>(request.getActorIds())
				: new java.util.HashSet<>());

		movie.setDirectorIds(request.getDirectorIds() != null ? new java.util.HashSet<>(request.getDirectorIds())
				: new java.util.HashSet<>());

		movie.setGenreIds(request.getGenreIds() != null ? new java.util.HashSet<>(request.getGenreIds())
				: new java.util.HashSet<>());

		Movie updatedMovie = movieRepository.update(movie);

		return MovieMapper.toResponse(updatedMovie);
	}

	// DELETE
	public void deleteMovie(String id) {

		Movie movie = movieRepository.findById(id);

		if (movie == null) {
			throw new RuntimeException("Movie not found with id: " + id);
		}

		movieRepository.deleteById(id);
	}

	// VALIDATE ACTORS
	private void validateActors(List<String> actorIds) {

		if (actorIds == null) {
			return;
		}

		for (String actorId : actorIds) {

			if (actorRepository.findById(actorId) == null) {

				throw new RuntimeException("Actor not found with id: " + actorId);
			}
		}
	}

	// VALIDATE DIRECTORS
	private void validateDirectors(List<String> directorIds) {

		if (directorIds == null) {
			return;
		}

		for (String directorId : directorIds) {

			if (directorRepository.findById(directorId) == null) {

				throw new RuntimeException("Director not found with id: " + directorId);
			}
		}
	}

	// VALIDATE GENRES
	private void validateGenres(List<String> genreIds) {

		if (genreIds == null) {
			return;
		}

		for (String genreId : genreIds) {

			if (genreRepository.findById(genreId) == null) {

				throw new RuntimeException("Genre not found with id: " + genreId);
			}
		}
	}
}