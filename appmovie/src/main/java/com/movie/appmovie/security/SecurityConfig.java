package com.movie.appmovie.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				// --------------------------------------
				// CSRF
				// --------------------------------------

				.csrf(csrf -> csrf.disable())

				// --------------------------------------
				// STATELESS
				// --------------------------------------

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// --------------------------------------
				// AUTHORIZATION
				// --------------------------------------

				.authorizeHttpRequests(auth -> auth

						// Authentication
						.requestMatchers("/auth/login").permitAll().requestMatchers("/auth/register").permitAll()

						// Public movie browsing
						.requestMatchers("/movies", "/movies/**").permitAll()

						// Public actors/directors/genres if needed
						.requestMatchers("/actors", "/actors/**").permitAll()
						.requestMatchers("/directors", "/directors/**").permitAll()
						.requestMatchers("/genres", "/genres/**").permitAll()

						// Everything else requires JWT
						.anyRequest().authenticated())
				// --------------------------------------
				// JWT FILTER
				// --------------------------------------

				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// --------------------------------------
	// PASSWORD ENCODER
	// --------------------------------------

	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}
}