import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { userApi, session } from '../services/api';

const initialForm = {
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  dateOfBirth: '',
  country: '',
};

export default function Register() {
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState(null);
  const [busy, setBusy] = useState(false);
  const navigate = useNavigate();

  const update = (field) => (e) => setForm((f) => ({ ...f, [field]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setBusy(true);
    try {
      // Matches UserRequest exactly: firstName, lastName, email, password,
      // dateOfBirth, country. Backend throws if the email is already
      // registered (UserService.createUser).
      const created = await userApi.create(form);
      session.set(created);
      navigate('/');
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="page">
      <div className="card auth-wrap">
        <span className="eyebrow">Join Reel</span>
        <h1 className="marquee" style={{ margin: '8px 0 24px' }}>
          Create account
        </h1>

        {error && <div className="form-error">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-field">
              <label htmlFor="firstName">First name</label>
              <input id="firstName" required value={form.firstName} onChange={update('firstName')} />
            </div>
            <div className="form-field">
              <label htmlFor="lastName">Last name</label>
              <input id="lastName" required value={form.lastName} onChange={update('lastName')} />
            </div>
          </div>

          <div className="form-field">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              required
              value={form.email}
              onChange={update('email')}
              placeholder="you@example.com"
            />
          </div>

          <div className="form-field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              required
              value={form.password}
              onChange={update('password')}
              placeholder="••••••••"
            />
          </div>

          <div className="form-row">
            <div className="form-field">
              <label htmlFor="dateOfBirth">Date of birth</label>
              <input id="dateOfBirth" type="date" value={form.dateOfBirth} onChange={update('dateOfBirth')} />
            </div>
            <div className="form-field">
              <label htmlFor="country">Country</label>
              <input id="country" value={form.country} onChange={update('country')} placeholder="India" />
            </div>
          </div>

          <button type="submit" className="btn btn-primary btn-block" disabled={busy} style={{ marginTop: 8 }}>
            {busy ? 'Creating account…' : 'Create account'}
          </button>
        </form>

        <p className="meta" style={{ marginTop: 20, textAlign: 'center' }}>
          Already have an account?{' '}
          <Link to="/login" style={{ color: 'var(--gold)' }}>
            Sign in
          </Link>
        </p>
      </div>
    </div>
  );
}
