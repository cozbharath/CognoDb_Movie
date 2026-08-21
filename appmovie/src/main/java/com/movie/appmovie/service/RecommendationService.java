package com.movie.appmovie.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.movie.appmovie.repository.RecommendationRepository;
import com.movie.appmovie.repository.UserRepository;
import com.movie.appmovie.response.RecommendationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

	private final RecommendationRepository recommendationRepository;

	private final UserRepository userRepository;

	// =========================================================
	// GET RECOMMENDATIONS
	// =========================================================

	public List<RecommendationResponse> getRecommendations(String userId) {

		// Check whether user exists
		if (userRepository.findById(userId) == null) {

			throw new RuntimeException("User not found with id: " + userId);
		}

		return recommendationRepository.getRecommendations(userId);
	}
}