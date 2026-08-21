package com.movie.appmovie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.appmovie.entity.Genre;
import com.movie.appmovie.mapper.GenreMapper;
import com.movie.appmovie.repository.GenreRepository;
import com.movie.appmovie.request.GenreRequest;
import com.movie.appmovie.response.GenreResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {

	private final GenreRepository genreRepository;

	// CREATE
	public GenreResponse createGenre(GenreRequest request) {

		Genre genre = GenreMapper.toEntity(request);

		Genre savedGenre = genreRepository.save(genre);

		return GenreMapper.toResponse(savedGenre);
	}

	// GET ALL
	public List<GenreResponse> getAllGenres() {

		return genreRepository.findAll().stream().map(GenreMapper::toResponse).toList();
	}

	// GET BY ID
	public GenreResponse getGenreById(String id) {

		Genre genre = genreRepository.findById(id);

		if (genre == null) {
			throw new RuntimeException("Genre not found with id: " + id);
		}

		return GenreMapper.toResponse(genre);
	}

	// UPDATE
	public GenreResponse updateGenre(String id, GenreRequest request) {

		Genre genre = genreRepository.findById(id);

		if (genre == null) {
			throw new RuntimeException("Genre not found with id: " + id);
		}

		genre.setGenreName(request.getGenreName());
		genre.setDescription(request.getDescription());

		Genre updatedGenre = genreRepository.update(genre);

		return GenreMapper.toResponse(updatedGenre);
	}

	// DELETE
	public void deleteGenre(String id) {

		Genre genre = genreRepository.findById(id);

		if (genre == null) {
			throw new RuntimeException("Genre not found with id: " + id);
		}

		genreRepository.deleteById(id);
	}
}