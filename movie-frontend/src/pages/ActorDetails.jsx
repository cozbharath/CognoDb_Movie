import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { actorApi, movieApi } from '../services/api';
import MovieCard from '../components/MovieCard';

function initialsOf(first = '', last = '') {
  return `${first[0] ?? ''}${last[0] ?? ''}`.toUpperCase() || '?';
}

export default function ActorDetails() {
  const { id } = useParams();
  const [actor, setActor] = useState(null);
  const [filmography, setFilmography] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    Promise.all([actorApi.getById(id), movieApi.getAll()])
      .then(([a, movies]) => {
        setActor(a);
        setFilmography(movies.filter((m) => m.actorIds?.includes(id)));
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [id]);

  if (loading) {
    return (
      <div className="page">
        <div className="state-block">
          <div className="spinner" />
          Loading actor…
        </div>
      </div>
    );
  }

  if (error || !actor) {
    return (
      <div className="page">
        <div className="form-error">{error || 'Actor not found.'}</div>
        <Link to="/actors" className="btn btn-ghost" style={{ marginTop: 16 }}>
          ← Back to actors
        </Link>
      </div>
    );
  }

  return (
    <div className="page">
      <Link to="/actors" className="meta" style={{ display: 'inline-block', marginBottom: 24 }}>
        ← Back to actors
      </Link>

      <div style={{ display: 'flex', gap: 24, alignItems: 'center', marginBottom: 12 }}>
        <div
          style={{
            width: 96,
            height: 96,
            borderRadius: '50%',
            background: 'linear-gradient(140deg, var(--gold), var(--crimson))',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontFamily: 'var(--font-display)',
            fontSize: 36,
            color: 'var(--void)',
            flexShrink: 0,
          }}
        >
          {initialsOf(actor.firstName, actor.lastName)}
        </div>
        <div>
          <span className="eyebrow">Actor</span>
          <h1 className="marquee" style={{ margin: '6px 0 4px' }}>
            {actor.firstName} {actor.lastName}
          </h1>
          <div className="meta">
            {actor.nationality || 'Unknown origin'}
            {actor.dateOfBirth ? ` · Born ${actor.dateOfBirth}` : ''}
            {actor.gender ? ` · ${actor.gender}` : ''}
          </div>
        </div>
      </div>

      <div className="divider" />

      <div className="section-head">
        <h2 className="marquee">Filmography</h2>
        <span className="meta">{filmography.length} title{filmography.length === 1 ? '' : 's'}</span>
      </div>

      {filmography.length === 0 ? (
        <div className="state-block">No credited movies yet.</div>
      ) : (
        <div className="grid grid-4">
          {filmography.map((m) => (
            <MovieCard key={m.movieId} movie={m} />
          ))}
        </div>
      )}
    </div>
  );
}
