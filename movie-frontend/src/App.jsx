import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import Home from './pages/Home';
import Movies from './pages/Movies';
import MovieDetails from './pages/MovieDetails';
import Actors from './pages/Actors';
import ActorDetails from './pages/ActorDetails';
import Directors from './pages/Directors';
import Genres from './pages/Genres';
import Login from './pages/Login';
import Register from './pages/Register';

export default function App() {
  return (
    <BrowserRouter>
      <div className="app-shell">
        <Navbar />
        <div className="app-main">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/movies" element={<Movies />} />
            <Route path="/movies/:id" element={<MovieDetails />} />
            <Route path="/actors" element={<Actors />} />
            <Route path="/actors/:id" element={<ActorDetails />} />
            <Route path="/directors" element={<Directors />} />
            <Route path="/genres" element={<Genres />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
          </Routes>
          <Footer />
        </div>
      </div>
    </BrowserRouter>
  );
}
