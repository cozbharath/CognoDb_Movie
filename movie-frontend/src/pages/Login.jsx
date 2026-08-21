import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authApi, session } from '../services/api';

export default function Login() {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const [error, setError] = useState(null);
  const [busy, setBusy] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {

    e.preventDefault();

    setError(null);
    setBusy(true);

    try {

      // ==============================
      // 1. LOGIN TO BACKEND
      // ==============================

      const response = await authApi.login({
        email: email,
        password: password
      });

      console.log("LOGIN RESPONSE:", response);

      // ==============================
      // 2. GET JWT TOKEN
      // ==============================

      const token =
        typeof response === 'string'
          ? response
          : response.token;

      if (!token) {
        throw new Error('JWT token was not returned by backend.');
      }

      // ==============================
      // 3. STORE TOKEN
      // ==============================

      session.setToken(token);

      console.log("JWT STORED");

      // ==============================
      // 4. STORE USER INFO
      // ==============================

      session.set({
        email: email
      });

      // ==============================
      // 5. GO HOME
      // ==============================

      navigate('/');

    } catch (err) {

      console.error("LOGIN ERROR:", err);

      setError(err.message);

    } finally {

      setBusy(false);

    }
  };

  return (
    <div className="page">

      <div className="card auth-wrap">

        <span className="eyebrow">
          Welcome back
        </span>

        <h1
          className="marquee"
          style={{ margin: '8px 0 24px' }}
        >
          Sign in
        </h1>

        {error && (
          <div className="form-error">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>

          <div className="form-field">

            <label htmlFor="email">
              Email
            </label>

            <input
              id="email"
              type="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="you@example.com"
            />

          </div>

          <div className="form-field">

            <label htmlFor="password">
              Password
            </label>

            <input
              id="password"
              type="password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
            />

          </div>

          <button
            type="submit"
            className="btn btn-primary btn-block"
            disabled={busy}
            style={{ marginTop: 8 }}
          >
            {busy ? 'Signing in…' : 'Sign in'}
          </button>

        </form>

        <p
          className="meta"
          style={{
            marginTop: 20,
            textAlign: 'center'
          }}
        >

          New here?{' '}

          <Link
            to="/register"
            style={{ color: 'var(--gold)' }}
          >
            Create an account
          </Link>

        </p>

      </div>

    </div>
  );
}