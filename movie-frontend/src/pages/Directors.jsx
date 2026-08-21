import { useEffect, useMemo, useState } from 'react';
import { directorApi, movieApi } from '../services/api';

function initialsOf(first = '', last = '') {
  return `${first[0] ?? ''}${last[0] ?? ''}`.toUpperCase() || '?';
}

export default function Directors() {
  const [directors, setDirectors] = useState([]);
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [query, setQuery] = useState('');

  useEffect(() => {
    Promise.all([directorApi.getAll(), movieApi.getAll()])
      .then(([d, m]) => {
        setDirectors(d);
        setMovies(m);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  const filtered = useMemo(() => {
    const q = query.toLowerCase();
    return directors.filter((d) => `${d.firstName} ${d.lastName}`.toLowerCase().includes(q));
  }, [directors, query]);

  const creditsFor = (directorId) => movies.filter((m) => m.directorIds?.includes(directorId)).length;

  return (
    <div className="page">
      <div className="section-head">
        <div>
          <span className="eyebrow">Behind the camera</span>
          <h1 className="marquee" style={{ marginTop: 8 }}>
            Directors
          </h1>
        </div>
      </div>

      <div className="toolbar">
        <input
          type="text"
          placeholder="Search directors…"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      {loading && (
        <div className="state-block">
          <div className="spinner" />
          Loading directors…
        </div>
      )}

      {error && <div className="form-error">Couldn't reach the backend: {error}</div>}

      {!loading && !error && filtered.length === 0 && <div className="state-block">No directors found.</div>}

      {!loading && !error && filtered.length > 0 && (
        <div className="grid grid-3">
          {filtered.map((d) => (
            <div key={d.directorId} className="card person-card">
              <div className="person-avatar">{initialsOf(d.firstName, d.lastName)}</div>
              <div style={{ minWidth: 0 }}>
                <div className="person-name">
                  {d.firstName} {d.lastName}
                </div>
                <div className="person-meta">
                  {d.nationality || 'Unknown origin'} · {creditsFor(d.directorId)} credit
                  {creditsFor(d.directorId) === 1 ? '' : 's'}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
