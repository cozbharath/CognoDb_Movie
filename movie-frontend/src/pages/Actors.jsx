import { useEffect, useMemo, useState } from 'react';
import { actorApi } from '../services/api';
import ActorCard from '../components/ActorCard';

export default function Actors() {
  const [actors, setActors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [query, setQuery] = useState('');

  useEffect(() => {
    actorApi
      .getAll()
      .then(setActors)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  const filtered = useMemo(() => {
    const q = query.toLowerCase();
    return actors.filter((a) => `${a.firstName} ${a.lastName}`.toLowerCase().includes(q));
  }, [actors, query]);

  return (
    <div className="page">
      <div className="section-head">
        <div>
          <span className="eyebrow">Cast directory</span>
          <h1 className="marquee" style={{ marginTop: 8 }}>
            Actors
          </h1>
        </div>
      </div>

      <div className="toolbar">
        <input type="text" placeholder="Search actors…" value={query} onChange={(e) => setQuery(e.target.value)} />
      </div>

      {loading && (
        <div className="state-block">
          <div className="spinner" />
          Loading actors…
        </div>
      )}

      {error && <div className="form-error">Couldn't reach the backend: {error}</div>}

      {!loading && !error && filtered.length === 0 && <div className="state-block">No actors found.</div>}

      {!loading && !error && filtered.length > 0 && (
        <div className="grid grid-3">
          {filtered.map((a) => (
            <ActorCard key={a.actorId} actor={a} />
          ))}
        </div>
      )}
    </div>
  );
}
