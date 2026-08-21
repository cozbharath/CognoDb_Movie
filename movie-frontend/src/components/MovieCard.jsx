import { Link } from 'react-router-dom';

function initialsOf(title = '') {
  return title.trim().slice(0, 1).toUpperCase() || '?';
}

function yearOf(dateStr) {
  if (!dateStr) return '—';
  return String(dateStr).slice(0, 4);
}

export default function MovieCard({ movie }) {
  return (
    <Link to={`/movies/${movie.movieId}`} className="movie-card">
      <div className="movie-poster">
        {movie.rating != null && <span className="movie-rating-badge">★ {movie.rating.toFixed(1)}</span>}
        <span className="movie-poster-initial">{initialsOf(movie.title)}</span>
      </div>
      <div className="movie-card-body">
        <div className="movie-title">{movie.title}</div>
        <div className="movie-sub">
          <span>{yearOf(movie.releaseDate)}</span>
          {movie.durationMinutes && <span>· {movie.durationMinutes}m</span>}
          {movie.language && <span>· {movie.language}</span>}
        </div>
      </div>
    </Link>
  );
}
