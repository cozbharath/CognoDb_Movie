import { Link } from 'react-router-dom';

function initialsOf(first = '', last = '') {
  return `${first[0] ?? ''}${last[0] ?? ''}`.toUpperCase() || '?';
}

function ageOf(dob) {
  if (!dob) return null;
  const birth = new Date(dob);
  if (Number.isNaN(birth.getTime())) return null;
  const diff = Date.now() - birth.getTime();
  return Math.floor(diff / (1000 * 60 * 60 * 24 * 365.25));
}

export default function ActorCard({ actor }) {
  const age = ageOf(actor.dateOfBirth);
  return (
    <Link to={`/actors/${actor.actorId}`} className="card person-card">
      <div className="person-avatar">{initialsOf(actor.firstName, actor.lastName)}</div>
      <div style={{ minWidth: 0 }}>
        <div className="person-name">
          {actor.firstName} {actor.lastName}
        </div>
        <div className="person-meta">
          {actor.nationality || 'Unknown origin'}
          {age != null ? ` · ${age} yrs` : ''}
        </div>
      </div>
    </Link>
  );
}
