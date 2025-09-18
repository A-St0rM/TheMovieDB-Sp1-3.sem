# 🎬 Movie Database Backend

Dette projekt er et backend-system, der kan hente, gemme og håndtere data om film fra **The Movie Database API (TMDb)**.  
Formålet er at træne dataindsamling, DTO’er, JPA, relationer og brug af eksterne REST API’er.

Projektet har et scope på 4 arbejdsdage.

---

## 🚀 Funktionalitet

Backend kan:

- Hente **alle danske film udgivet de sidste 5 år** via TMDb API’et og gemme dem i en **PostgreSQL database**.
- Gemme relationer mellem:
    - 🎥 **Film**
    - 🎭 **Actors**
    - 🎬 **Directors**
    - 🏷 **Genres**
- Se en liste over:
    - Alle film i databasen
    - Alle skuespillere, instruktører og genrer
- Søge efter film baseret på titel (**case-insensitive**).
- Håndtere CRUD på film:
    - Oprette
    - Opdatere (titel og releasedato)
    - Slette
- Statistikfunktioner:
    - 📊 Gennemsnitlig rating af alle film
    - 🔝 Top-10 mest populære film
    - ⭐ Top-10 højst ratede film
    - 👎 Top-10 lavest ratede film
- Liste alle film i en bestemt genre.

---

## 🏗️ Teknologier

Projektet er udviklet i **Java** uden Spring Boot, men med:

- **Hibernate/JPA** → ORM og persistence
- **PostgreSQL** → Database
- **Jackson** → JSON (serialisering og deserialisering)
- **Lombok** → Boilerplate-reduktion (`@Data`, `@Builder` osv.)
- **TMDb REST API** → Ekstern datakilde
- **Maven** → Build og dependencies

---

## 📂 Projektstruktur

src/main/java/app

├── config/ # Hibernate config

├── dao/ # DAO-klasser til persistence

├── dto/ # DTO-klasser (til JSON fra TMDb)

├── entities/ # Entities (Movie, Actor, Director, Genre)

├── service/ # Services med forretningslogik

├── utils/ # Hjælpeklasser (fx TMDbClient)

└── Main.java # Main-program til datafetch og demo

---

## ⚙️ Installation & Opsætning

### 1. Klargør database
Projektet bruger PostgreSQL. Opret en database lokalt, fx:

```sql
CREATE DATABASE moviedb;
```

### 2. Konfigurer config.properties
Opret en config.properties i resources/ med dine DB- og API-oplysninger:
```
DB_NAME=moviedb
DB_USERNAME=postgres
DB_PASSWORD=your_password
TMDB_API_KEY=your_tmdb_api_key
```
### 3. Kør programmet

Første gang du kører programmet, vil det:

Hente data fra TMDb (danske film, sidste 5 år).

Gemme alt i databasen.

Udskrive queries i konsolle
