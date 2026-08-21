import { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { genreApi, movieApi } from '../services/api';

export default function Genres() {
  const [genres, setGenres] = useState([]);
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    Promise.all([genreApi.getAll(), movieApi.getAll()])
      .then(([g, m]) => {
        setGenres(g);
        setMovies(m);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  const countFor = (genreId) => movies.filter((m) => m.genreIds?.includes(genreId)).length;

  return (
    <div className="page">
      <div className="section-head">
        <div>
          <span className="eyebrow">Explore by mood</span>
          <h1 className="marquee" style={{ marginTop: 8 }}>
            Genres
          </h1>
        </div>
      </div>

      {loading && (
        <div className="state-block">
          <div className="spinner" />
          Loading genres…
        </div>
      )}

      {error && <div className="form-error">Couldn't reach the backend: {error}</div>}

      {!loading && !error && genres.length === 0 && <div className="state-block">No genres yet.</div>}

      {!loading && !error && genres.length > 0 && (
        <div className="grid grid-3">
          {genres.map((g) => (
            <div key={g.genreId} className="card genre-card" onClick={() => navigate(`/movies?genre=${g.genreId}`)}>
              <div className="genre-name">{g.genreName}</div>
              <p className="meta" style={{ margin: 0 }}>
                {g.description || 'No description yet.'}
              </p>
              <span className="chip chip-gold" style={{ alignSelf: 'flex-start', marginTop: 6 }}>
                {countFor(g.genreId)} title{countFor(g.genreId) === 1 ? '' : 's'}
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
