import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { movieApi, session } from '../services/api';
import MovieCard from '../components/MovieCard';

export default function Home() {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const user = session.get();

  useEffect(() => {
    movieApi
      .getAll()
      .then((data) => setMovies(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  const topRated = [...movies].sort((a, b) => (b.rating ?? 0) - (a.rating ?? 0)).slice(0, 8);

  return (
    <div className="page">
      <section className="hero">
        <span className="eyebrow">Movie recommendation graph</span>
        <h1 className="marquee" style={{ marginTop: 10, marginBottom: 16 }}>
          Find your next
          <br />
          obsession.
        </h1>
        <p className="lede">
          Explore films through the people who made them — trace a lead actor to their director, a genre to its best
          titles, and let the graph surface what to watch next.
        </p>
        <div className="hero-cta">
          <Link to="/movies" className="btn btn-primary">
            Browse movies
          </Link>
          {!user && (
            <Link to="/register" className="btn btn-ghost">
              Create an account
            </Link>
          )}
        </div>
      </section>

      <div className="divider" />

      <div className="section-head">
        <h2 className="marquee">Top Rated</h2>
        <span className="meta">{movies.length} titles in the graph</span>
      </div>

      {loading && (
        <div className="state-block">
          <div className="spinner" />
          Loading the marquee…
        </div>
      )}

      {error && <div className="form-error">Couldn't reach the backend: {error}</div>}

      {!loading && !error && topRated.length === 0 && (
        <div className="state-block">No movies yet — add one from the backend to see it here.</div>
      )}

      {!loading && !error && topRated.length > 0 && (
        <div className="grid grid-4">
          {topRated.map((m) => (
            <MovieCard key={m.movieId} movie={m} />
          ))}
        </div>
      )}
    </div>
  );
}
