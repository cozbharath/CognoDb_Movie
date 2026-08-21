# 🎬 CognoDb_Movie

A full-stack movie application built with modern technologies, combining a **Spring Boot backend** with **Neo4j graph database** and a **React frontend** with **Vite**.

## 📋 Overview

CognoDb_Movie is a comprehensive movie management application that leverages:
- **Neo4j** for graph-based relationships between movies, actors, directors, and genres
- **Spring Boot 4.1.0** for RESTful API backend with security features
- **React 18** with **Vite** for fast, modern frontend development
- **JWT Authentication** for secure user access

This project demonstrates a modern full-stack architecture with separation of concerns between the backend API and frontend UI.

---

## 📁 Project Structure

```
CognoDb_Movie/
├── appmovie/                 # Spring Boot Backend
│   ├── src/                  # Java source code
│   ├── pom.xml              # Maven configuration
│   ├── mvnw                 # Maven wrapper (Linux/Mac)
│   └── mvnw.cmd             # Maven wrapper (Windows)
│
└── movie-frontend/           # React Frontend
    ├── src/                  # React components and pages
    ├── index.html           # HTML entry point
    ├── package.json         # NPM dependencies
    ├── vite.config.js       # Vite configuration
    └── node_modules/        # Dependencies
```

---

## 🔧 Technology Stack

### Backend (appmovie)
| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 21 | Runtime |
| **Spring Boot** | 4.1.0 | Framework |
| **Neo4j Driver** | Latest | Graph Database |
| **Spring Security** | Latest | Authentication |
| **JWT (JJWT)** | 0.13.0 | Token Management |
| **Lombok** | Latest | Code Generation |
| **Maven** | Bundled | Build Tool |

### Frontend (movie-frontend)
| Technology | Version | Purpose |
|-----------|---------|---------|
| **React** | 18.3.1 | UI Framework |
| **React Router** | 6.28.0 | Navigation |
| **Axios** | 1.7.9 | HTTP Client |
| **Vite** | 5.4.11 | Build Tool |
| **Node.js** | 14+ | Runtime |

---

## 🚀 Getting Started

### Prerequisites
- **Java 21** or higher
- **Node.js 14+** and **npm**
- **Neo4j Database** (running instance with connection details)

### Backend Setup (Spring Boot)

1. **Navigate to the backend directory:**
   ```bash
   cd appmovie
   ```

2. **Configure Neo4j connection:**
   - Update `application.properties` or `application.yml` in `src/main/resources/`
   - Add Neo4j connection details:
     ```properties
     spring.neo4j.uri=bolt://localhost:7687
     spring.neo4j.authentication.username=neo4j
     spring.neo4j.authentication.password=your_password
     ```

3. **Build the project:**
   ```bash
   ./mvnw clean package
   ```

4. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   
   The backend will start on `http://localhost:8080`

### Frontend Setup (React + Vite)

1. **Navigate to the frontend directory:**
   ```bash
   cd movie-frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Create environment configuration:**
   - Create a `.env` file in `movie-frontend/`:
     ```
     VITE_API_URL=http://localhost:8080
     ```

4. **Start development server:**
   ```bash
   npm run dev
   ```
   
   The frontend will be available at `http://localhost:5173`

5. **Build for production:**
   ```bash
   npm run build
   ```

---

## 🔐 Security Features

- **Spring Security** integration for protected endpoints
- **JWT (JSON Web Tokens)** for stateless authentication
- **Input Validation** using Spring Boot validation framework
- **CORS** support for frontend-backend communication

---

## 📡 API Endpoints

The backend provides RESTful API endpoints for movie management. Key features:

- **Authentication**: Login and token generation
- **Movies**: CRUD operations with Neo4j relationships
- **Actors & Directors**: Relationship management
- **Genres**: Classification and filtering
- **Search & Filter**: Query movies by various criteria

*For detailed API documentation, refer to controller classes in `appmovie/src/main/java/`*

---

## 🛠️ Development Scripts

### Backend Commands
```bash
cd appmovie

# Build
./mvnw clean package

# Run with Spring Boot Maven Plugin
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run on Windows
mvnw.cmd clean package
mvnw.cmd spring-boot:run
```

### Frontend Commands
```bash
cd movie-frontend

# Install dependencies
npm install

# Development server (with HMR)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

---

## 📦 Dependencies

### Backend Key Dependencies
- `spring-boot-starter-webmvc` - Web & MVC support
- `spring-boot-starter-security` - Security framework
- `spring-boot-starter-validation` - Input validation
- `neo4j-java-driver` - Neo4j database driver
- `jjwt` - JWT token handling
- `lombok` - Annotation processor for code generation

### Frontend Key Dependencies
- `react` - UI library
- `react-router-dom` - Client-side routing
- `axios` - HTTP client for API calls
- `vite` - Next generation build tool

---

## 🧪 Testing

### Backend Testing
```bash
cd appmovie
./mvnw test
```

---

## 📋 Configuration Files

### Backend
- **pom.xml** - Maven project configuration and dependencies
- **application.properties** - Spring Boot configuration (create if needed)

### Frontend
- **package.json** - NPM packages and scripts
- **vite.config.js** - Vite bundler configuration
- **.gitignore** - Git ignore rules

---

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

---

## 📝 License

This project is open source and available under the MIT License.

---

## 💡 Features

- ✅ Graph-based movie database with Neo4j
- ✅ Secure JWT authentication
- ✅ RESTful API backend
- ✅ Modern React frontend with Vite
- ✅ Responsive UI with React Router navigation
- ✅ Real-time HTTP communication with Axios
- ✅ Spring Security integration
- ✅ Input validation and error handling

---

## 🐛 Troubleshooting

### Backend Issues
- **Neo4j Connection Failed**: Verify Neo4j is running and credentials are correct
- **Port Already in Use**: Change server port in `application.properties`: `server.port=8081`
- **Java Version Mismatch**: Ensure Java 21 is installed: `java -version`

### Frontend Issues
- **Dependencies Not Installing**: Clear cache: `npm cache clean --force` then `npm install`
- **Port Conflict**: Change Vite port in `vite.config.js`
- **API Connection Errors**: Verify backend is running and `VITE_API_URL` is correct

---

## 👤 Author & Contact

**Bharath Kumar**
- GitHub: [@cozbharath](https://github.com/cozbharath)
- Project: [CognoDb_Movie](https://github.com/cozbharath/CognoDb_Movie)

For questions, suggestions, or collaboration, feel free to reach out or open an issue on the repository.

---
