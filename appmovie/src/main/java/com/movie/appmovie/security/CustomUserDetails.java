package com.movie.appmovie.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.movie.appmovie.entity.User;

public class CustomUserDetails implements UserDetails {

	private final User user;

	public CustomUserDetails(User user) {
		this.user = user;
	}

	// -----------------------------------------
	// USERNAME
	// -----------------------------------------

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	// -----------------------------------------
	// PASSWORD
	// -----------------------------------------

	@Override
	public String getPassword() {
		return user.getPasswordHash();
	}

	// -----------------------------------------
	// AUTHORITIES
	// -----------------------------------------

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	// -----------------------------------------
	// ACCOUNT STATUS
	// -----------------------------------------

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	// -----------------------------------------
	// OPTIONAL
	// -----------------------------------------

	public User getUser() {
		return user;
	}
}