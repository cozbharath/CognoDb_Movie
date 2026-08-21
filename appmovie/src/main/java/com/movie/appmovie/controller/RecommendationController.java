package com.movie.appmovie.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.appmovie.response.RecommendationResponse;
import com.movie.appmovie.service.RecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

	private final RecommendationService recommendationService;

	// =========================================================
	// GET MOVIE RECOMMENDATIONS FOR USER
	// =========================================================

	@GetMapping("/{userId}")
	public ResponseEntity<List<RecommendationResponse>> getRecommendations(@PathVariable String userId) {

		return ResponseEntity.ok(recommendationService.getRecommendations(userId));
	}
}