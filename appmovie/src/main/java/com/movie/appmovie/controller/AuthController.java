package com.movie.appmovie.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.appmovie.entity.User;
import com.movie.appmovie.request.LoginRequest;
import com.movie.appmovie.request.UserRequest;
import com.movie.appmovie.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	// ==============================
	// REGISTER
	// ==============================

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody UserRequest request) {

		User savedUser = authService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	// ==============================
	// LOGIN
	// ==============================

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest request) {

		String token = authService.login(request);

		return ResponseEntity.ok(token);
	}
}