import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { movieApi, actorApi, directorApi, genreApi, userApi, recommendationApi, session } from '../services/api';
import MovieCard from '../components/MovieCard';

export default function MovieDetails() {
  const { id } = useParams();
  const [movie, setMovie] = useState(null);
  const [actors, setActors] = useState([]);
  const [directors, setDirectors] = useState([]);
  const [genres, setGenres] = useState([]);
  const [recs, setRecs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [watched, setWatched] = useState(false);
  const [busy, setBusy] = useState(false);
  const user = session.get();

  useEffect(() => {
    setLoading(true);
    Promise.all([movieApi.getById(id), actorApi.getAll(), directorApi.getAll(), genreApi.getAll()])
      .then(([m, a, d, g]) => {
        setMovie(m);
        setActors(a);
        setDirectors(d);
        setGenres(g);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [id]);

  useEffect(() => {
    if (user) {
      userApi
        .getById(user.userId)
        .then((u) => setWatched(u.watchedMovieIds?.includes(id) ?? false))
        .catch(() => {});
      recommendationApi
        .getForUser(user.userId)
        .then((data) => setRecs(data.filter((r) => r.movieId !== id).slice(0, 4)))
        .catch(() => {});
    }
  }, [user, id]);

  const toggleWatched = async () => {
    if (!user) return;
    setBusy(true);
    try {
      if (watched) {
        await userApi.removeWatched(user.userId, id);
        setWatched(false);
      } else {
        await userApi.markWatched(user.userId, id);
        setWatched(true);
      }
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  };

  if (loading) {
    return (
      <div className="page">
        <div className="state-block">
          <div className="spinner" />
          Loading title…
        </div>
      </div>
    );
  }

  if (error || !movie) {
    return (
      <div className="page">
        <div className="form-error">{error || 'Movie not found.'}</div>
        <Link to="/movies" className="btn btn-ghost" style={{ marginTop: 16 }}>
          ← Back to movies
        </Link>
      </div>
    );
  }

  const movieActors = actors.filter((a) => movie.actorIds?.includes(a.actorId));
  const movieDirectors = directors.filter((d) => movie.directorIds?.includes(d.directorId));
  const movieGenres = genres.filter((g) => movie.genreIds?.includes(g.genreId));

  return (
    <div className="page">
      <Link to="/movies" className="meta" style={{ display: 'inline-block', marginBottom: 24 }}>
        ← Back to movies
      </Link>

      <div className="detail-hero">
        <div className="detail-poster">{movie.title?.slice(0, 1).toUpperCase()}</div>

        <div>
          <span className="eyebrow">{movie.country || 'Feature Film'}</span>
          <h1 className="marquee" style={{ margin: '8px 0 4px' }}>
            {movie.title}
          </h1>
          <div className="meta">
            {movie.releaseDate?.slice(0, 4) || '—'} · {movie.durationMinutes ? `${movie.durationMinutes} min` : '—'}{' '}
            · {movie.language || '—'}
            {movie.rating != null && ` · ★ ${movie.rating.toFixed(1)}`}
          </div>

          <div className="tag-row">
            {movieGenres.map((g) => (
              <Link key={g.genreId} to={`/movies?genre=${g.genreId}`} className="chip chip-gold">
                {g.genreName}
              </Link>
            ))}
          </div>

          <p className="lede">{movie.description || 'No synopsis available yet.'}</p>

          {movieDirectors.length > 0 && (
            <p className="meta" style={{ marginTop: 14 }}>
              Directed by{' '}
              {movieDirectors.map((d, i) => (
                <span key={d.directorId}>
                  <Link to={`/directors`} style={{ color: 'var(--gold-bright)' }}>
                    {d.firstName} {d.lastName}
                  </Link>
                  {i < movieDirectors.length - 1 ? ', ' : ''}
                </span>
              ))}
            </p>
          )}

          {user ? (
            <button
              className={`btn ${watched ? 'btn-danger' : 'btn-primary'}`}
              style={{ marginTop: 20 }}
              disabled={busy}
              onClick={toggleWatched}
            >
              {watched ? '✕ Remove from watched' : '✓ Mark as watched'}
            </button>
          ) : (
            <p className="meta" style={{ marginTop: 20 }}>
              <Link to="/login" style={{ color: 'var(--gold)' }}>
                Sign in
              </Link>{' '}
              to track this as watched.
            </p>
          )}
        </div>
      </div>

      <div className="divider" />

      <div className="section-head">
        <h2 className="marquee">Cast</h2>
      </div>
      <div className="cast-list">
        {movieActors.length === 0 && <span className="meta">No cast listed.</span>}
        {movieActors.map((a) => (
          <Link key={a.actorId} to={`/actors/${a.actorId}`} className="chip">
            {a.firstName} {a.lastName}
          </Link>
        ))}
      </div>

      {user && recs.length > 0 && (
        <>
          <div className="divider" />
          <div className="section-head">
            <h2 className="marquee">You might also like</h2>
          </div>
          <div className="grid grid-4">
            {recs.map((r) => (
              <MovieCard key={r.movieId} movie={r} />
            ))}
          </div>
        </>
      )}
    </div>
  );
}
