import { useEffect, useMemo, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { movieApi, genreApi } from '../services/api';
import MovieCard from '../components/MovieCard';

export default function Movies() {
  const [movies, setMovies] = useState([]);
  const [genres, setGenres] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [query, setQuery] = useState('');
  const [searchParams, setSearchParams] = useSearchParams();
  const genreFilter = searchParams.get('genre') || '';

  useEffect(() => {
    Promise.all([movieApi.getAll(), genreApi.getAll()])
      .then(([m, g]) => {
        setMovies(m);
        setGenres(g);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  const filtered = useMemo(() => {
    return movies.filter((m) => {
      const matchesQuery = m.title?.toLowerCase().includes(query.toLowerCase());
      const matchesGenre = !genreFilter || m.genreIds?.includes(genreFilter);
      return matchesQuery && matchesGenre;
    });
  }, [movies, query, genreFilter]);

  const genreName = genres.find((g) => g.genreId === genreFilter)?.genreName;

  return (
    <div className="page">
      <div className="section-head">
        <div>
          <span className="eyebrow">Catalogue</span>
          <h1 className="marquee" style={{ marginTop: 8 }}>
            Movies
          </h1>
        </div>
      </div>

      <div className="toolbar">
        <input
          type="text"
          placeholder="Search by title…"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <select
          value={genreFilter}
          onChange={(e) => {
            const val = e.target.value;
            setSearchParams(val ? { genre: val } : {});
          }}
        >
          <option value="">All genres</option>
          {genres.map((g) => (
            <option key={g.genreId} value={g.genreId}>
              {g.genreName}
            </option>
          ))}
        </select>
        {genreFilter && (
          <button className="btn btn-ghost btn-sm" onClick={() => setSearchParams({})}>
            Clear: {genreName || 'genre'} ✕
          </button>
        )}
      </div>

      {loading && (
        <div className="state-block">
          <div className="spinner" />
          Loading movies…
        </div>
      )}

      {error && <div className="form-error">Couldn't reach the backend: {error}</div>}

      {!loading && !error && filtered.length === 0 && (
        <div className="state-block">No movies match your search.</div>
      )}

      {!loading && !error && filtered.length > 0 && (
        <div className="grid grid-4">
          {filtered.map((m) => (
            <MovieCard key={m.movieId} movie={m} />
          ))}
        </div>
      )}
    </div>
  );
}
