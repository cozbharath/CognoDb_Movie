package com.movie.appmovie.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreResponse {

	private String genreId;

	private String genreName;

	private String description;
}