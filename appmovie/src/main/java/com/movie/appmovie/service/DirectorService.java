package com.movie.appmovie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.appmovie.entity.Director;
import com.movie.appmovie.mapper.DirectorMapper;
import com.movie.appmovie.repository.DirectorRepository;
import com.movie.appmovie.request.DirectorRequest;
import com.movie.appmovie.response.DirectorResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DirectorService {

	private final DirectorRepository directorRepository;

	// CREATE
	public DirectorResponse createDirector(DirectorRequest request) {

		Director director = DirectorMapper.toEntity(request);

		Director savedDirector = directorRepository.save(director);

		return DirectorMapper.toResponse(savedDirector);
	}

	// GET ALL
	public List<DirectorResponse> getAllDirectors() {

		return directorRepository.findAll().stream().map(DirectorMapper::toResponse).toList();
	}

	// GET BY ID
	public DirectorResponse getDirectorById(String id) {

		Director director = directorRepository.findById(id);

		if (director == null) {
			throw new RuntimeException("Director not found with id: " + id);
		}

		return DirectorMapper.toResponse(director);
	}

	// UPDATE
	public DirectorResponse updateDirector(String id, DirectorRequest request) {

		Director director = directorRepository.findById(id);

		if (director == null) {
			throw new RuntimeException("Director not found with id: " + id);
		}

		director.setFirstName(request.getFirstName());
		director.setLastName(request.getLastName());
		director.setDateOfBirth(request.getDateOfBirth());
		director.setNationality(request.getNationality());

		Director updatedDirector = directorRepository.update(director);

		return DirectorMapper.toResponse(updatedDirector);
	}

	// DELETE
	public void deleteDirector(String id) {

		Director director = directorRepository.findById(id);

		if (director == null) {
			throw new RuntimeException("Director not found with id: " + id);
		}

		directorRepository.deleteById(id);
	}
}