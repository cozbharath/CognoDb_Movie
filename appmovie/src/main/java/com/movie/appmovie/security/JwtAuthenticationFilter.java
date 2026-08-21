package com.movie.appmovie.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	private final CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// ==========================================
		// 1. GET AUTHORIZATION HEADER
		// ==========================================

		String authorizationHeader = request.getHeader("Authorization");

		// ==========================================
		// 2. CHECK BEARER TOKEN
		// ==========================================

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

			filterChain.doFilter(request, response);
			return;
		}

		// ==========================================
		// 3. EXTRACT JWT
		// ==========================================

		String token = authorizationHeader.substring(7);

		try {

			// ==========================================
			// 4. GET EMAIL FROM TOKEN
			// ==========================================

			String email = jwtUtil.extractUsername(token);

			// ==========================================
			// 5. CHECK USER IS NOT ALREADY AUTHENTICATED
			// ==========================================

			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				// ==========================================
				// 6. GET USER FROM NEO4J
				// ==========================================

				UserDetails userDetails = userDetailsService.loadUserByUsername(email);

				// ==========================================
				// 7. VALIDATE JWT
				// ==========================================

				if (jwtUtil.validateToken(token, userDetails.getUsername())) {

					// ==========================================
					// 8. CREATE AUTHENTICATION
					// ==========================================

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					// ==========================================
					// 9. STORE AUTHENTICATION
					// ==========================================

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}

		} catch (Exception e) {

			System.out.println("JWT Authentication failed: " + e.getMessage());
		}

		// ==========================================
		// 10. CONTINUE REQUEST
		// ==========================================

		filterChain.doFilter(request, response);
	}
}