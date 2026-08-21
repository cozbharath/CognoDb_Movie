package com.movie.appmovie.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.movie.appmovie.entity.User;
import com.movie.appmovie.repository.UserRepository;
import com.movie.appmovie.request.LoginRequest;
import com.movie.appmovie.request.UserRequest;
import com.movie.appmovie.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	// ==============================
	// REGISTER
	// ==============================

	public User register(UserRequest request) {

		User existingUser = userRepository.findByEmail(request.getEmail());

		if (existingUser != null) {
			throw new RuntimeException("Email already registered");
		}

		User user = new User();

		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());

		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

		user.setDateOfBirth(request.getDateOfBirth());
		user.setCountry(request.getCountry());

		return userRepository.save(user);
	}

	// ==============================
	// LOGIN
	// ==============================

	public String login(LoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail());

		if (user == null) {
			throw new RuntimeException("Invalid email or password");
		}

		boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

		if (!passwordMatches) {
			throw new RuntimeException("Invalid email or password");
		}

		return jwtUtil.generateToken(user.getEmail());
	}
}