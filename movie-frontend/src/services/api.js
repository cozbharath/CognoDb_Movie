import axios from 'axios';

// =========================================================
// AXIOS CLIENT
// =========================================================

const client = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});


// =========================================================
// JWT REQUEST INTERCEPTOR
// =========================================================

client.interceptors.request.use(
  (config) => {

    // Do NOT send JWT for login/register
    const isAuthRequest =
      config.url?.includes('/auth/login') ||
      config.url?.includes('/auth/register');

    if (isAuthRequest) {
      return config;
    }

    const token = localStorage.getItem('token');

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },

  (error) => {
    return Promise.reject(error);
  }
);


// =========================================================
// RESPONSE INTERCEPTOR
// =========================================================

client.interceptors.response.use(
  (response) => response,

  (error) => {

    const message =
      error.response?.data?.message ||
      (typeof error.response?.data === 'string'
        ? error.response.data
        : null) ||
      error.message ||
      'Something went wrong';

    return Promise.reject(new Error(message));
  }
);
// =========================================================
// GENRES
// =========================================================

export const genreApi = {

  getAll: () =>
    client.get('/genres')
      .then((response) => response.data),

  getById: (id) =>
    client.get(`/genres/${id}`)
      .then((response) => response.data),

  create: (payload) =>
    client.post('/genres', payload)
      .then((response) => response.data),

  update: (id, payload) =>
    client.put(`/genres/${id}`, payload)
      .then((response) => response.data),

  remove: (id) =>
    client.delete(`/genres/${id}`),
};

export const movieApi = {

  getAll: () =>
    client.get('/movies')
      .then((response) => response.data),

  getById: (id) =>
    client.get(`/movies/${id}`)
      .then((response) => response.data),

  create: (payload) =>
    client.post('/movies', payload)
      .then((response) => response.data),

  update: (id, payload) =>
    client.put(`/movies/${id}`, payload)
      .then((response) => response.data),

  remove: (id) =>
    client.delete(`/movies/${id}`),
};

export const authApi = {

  register: (payload) =>
    client.post('/auth/register', payload)
      .then((response) => response.data),

  login: (payload) =>
    client.post('/auth/login', payload)
      .then((response) => response.data),
};

// =========================================================
// ACTORS
// =========================================================

export const actorApi = {

  getAll: () =>
    client.get('/actors')
      .then((response) => response.data),

  getById: (id) =>
    client.get(`/actors/${id}`)
      .then((response) => response.data),

  create: (payload) =>
    client.post('/actors', payload)
      .then((response) => response.data),

  update: (id, payload) =>
    client.put(`/actors/${id}`, payload)
      .then((response) => response.data),

  remove: (id) =>
    client.delete(`/actors/${id}`),
};

// =========================================================
// DIRECTORS
// =========================================================

export const directorApi = {

  getAll: () =>
    client.get('/directors')
      .then((response) => response.data),

  getById: (id) =>
    client.get(`/directors/${id}`)
      .then((response) => response.data),

  create: (payload) =>
    client.post('/directors', payload)
      .then((response) => response.data),

  update: (id, payload) =>
    client.put(`/directors/${id}`, payload)
      .then((response) => response.data),

  remove: (id) =>
    client.delete(`/directors/${id}`),
};

// =========================================================
// RECOMMENDATIONS
// =========================================================

export const recommendationApi = {

  getForUser: (userId) =>
    client.get(`/recommendations/${userId}`)
      .then((response) => response.data),
};

// =========================================================
// USERS
// =========================================================

export const userApi = {

  getAll: () =>
    client.get('/users')
      .then((response) => response.data),

  getById: (id) =>
    client.get(`/users/${id}`)
      .then((response) => response.data),

  create: (payload) =>
    client.post('/users', payload)
      .then((response) => response.data),

  update: (id, payload) =>
    client.put(`/users/${id}`, payload)
      .then((response) => response.data),

  remove: (id) =>
    client.delete(`/users/${id}`),

  markWatched: (userId, movieId) =>
    client.post(`/users/${userId}/watched/${movieId}`)
      .then((response) => response.data),

  removeWatched: (userId, movieId) =>
    client.delete(`/users/${userId}/watched/${movieId}`)
      .then((response) => response.data),
};



// =========================================================
// SESSION HELPERS
// =========================================================

const SESSION_KEY = 'reel_user';

export const session = {

  get: () => {
    const raw = localStorage.getItem(SESSION_KEY);
    return raw ? JSON.parse(raw) : null;
  },

  set: (user) => {
    localStorage.setItem(
      SESSION_KEY,
      JSON.stringify(user)
    );
  },

  clear: () => {
    localStorage.removeItem(SESSION_KEY);
  },

  setToken: (token) => {
    localStorage.setItem('token', token);
  },

  getToken: () => {
    return localStorage.getItem('token');
  },

  clearToken: () => {
    localStorage.removeItem('token');
  }
};


export default client;