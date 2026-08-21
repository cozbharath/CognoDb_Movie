import { NavLink, useNavigate } from 'react-router-dom';
import { session } from '../services/api';

const links = [
  { to: '/', label: 'Home', exact: true },
  { to: '/movies', label: 'Movies' },
  { to: '/actors', label: 'Actors' },
  { to: '/directors', label: 'Directors' },
  { to: '/genres', label: 'Genres' },
];

export default function Navbar() {
  const navigate = useNavigate();
  const user = session.get();

  const handleLogout = () => {
    session.clear();
    navigate('/login');
  };

  const initials = user ? `${user.firstName?.[0] ?? ''}${user.lastName?.[0] ?? ''}`.toUpperCase() : '';

  return (
    <nav className="sidebar">
      <div className="brand">
        <span className="brand-mark">REEL</span>
        <span className="brand-tag">Now Showing</span>
      </div>

      <div className="nav-list">
        <div className="nav-eyebrow">Browse</div>
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            end={link.exact}
            className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}
          >
            <span className="nav-dot" />
            {link.label}
          </NavLink>
        ))}
      </div>

      <div className="sidebar-footer">
        {user ? (
          <div className="sidebar-user">
            <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
              <div
                style={{
                  width: 34,
                  height: 34,
                  borderRadius: '50%',
                  background: 'linear-gradient(140deg, var(--gold), var(--crimson))',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontFamily: 'var(--font-display)',
                  fontSize: 14,
                  color: 'var(--void)',
                  flexShrink: 0,
                }}
              >
                {initials || '?'}
              </div>
              <div style={{ minWidth: 0 }}>
                <div className="sidebar-user-name">
                  {user.firstName} {user.lastName}
                </div>
                <div className="sidebar-user-email">{user.email}</div>
              </div>
            </div>
            <button className="btn btn-ghost btn-sm btn-block" style={{ marginTop: 12 }} onClick={handleLogout}>
              Sign out
            </button>
          </div>
        ) : (
          <div className="sidebar-auth-btns">
            <NavLink to="/login" className="btn btn-primary btn-sm btn-block">
              Sign in
            </NavLink>
            <NavLink to="/register" className="btn btn-ghost btn-sm btn-block">
              Create account
            </NavLink>
          </div>
        )}
      </div>
    </nav>
  );
}
