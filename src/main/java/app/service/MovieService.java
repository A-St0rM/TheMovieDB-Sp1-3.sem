package app.service;

import app.DAO.ActorDAO;
import app.DAO.DirectorDAO;
import app.DAO.GenreDAO;
import app.DAO.MovieDAO;
import app.DTO.MovieDetailsDTO;
import app.entities.Actor;
import app.entities.Director;
import app.entities.Genre;
import app.entities.Movie;

import java.util.Set;
import java.util.stream.Collectors;

public class MovieService {

    private final MovieDAO movieDAO = new MovieDAO();
    private final GenreDAO genreDAO = new GenreDAO();
    private final ActorDAO actorDAO = new ActorDAO();
    private final DirectorDAO directorDAO = new DirectorDAO();

    /**
     * Convert MovieDetailsDTO (+ credits) into a Movie entity and persist.
     */
    public void saveMovie(MovieDetailsDTO dto) {
        // Skip hvis filmen allerede findes
        if (movieDAO.findByTmdbId(dto.getId()) != null) {
            return;
        }

        // Convert genres
        Set<Genre> genres = dto.getGenres().stream()
                .map(g -> {
                    Genre existing = genreDAO.findByTmdbId(g.getId());
                    if (existing != null) return existing;
                    return Genre.builder()
                            .tmdbId(g.getId())
                            .name(g.getName())
                            .build();
                })
                .collect(Collectors.toSet());

        // Convert actors
        Set<Actor> actors = dto.getCredits().getCast().stream()
                .sorted((c1, c2) -> c1.getOrder().compareTo(c2.getOrder()))
                .limit(10)
                .map(c -> {
                    Actor existing = actorDAO.findByTmdbId((long) c.getId());
                    if (existing != null) return existing;
                    return Actor.builder()
                            .tmdbId((long) c.getId())
                            .name(c.getName())
                            .build();
                })
                .collect(Collectors.toSet());

        // Find director fra crew
        Director director = dto.getCredits().getCrew().stream()
                .filter(c -> c.getJob().equalsIgnoreCase("Director"))
                .findFirst()
                .map(d -> {
                    Director existing = directorDAO.findByTmdbId((long) d.getId());
                    if (existing != null) return existing;
                    return Director.builder()
                            .tmdbId((long) d.getId())
                            .name(d.getName())
                            .build();
                })
                .orElse(null);

        Movie movie = Movie.builder()
                .tmdbId(dto.getId())
                .title(dto.getTitle())
                .overview(dto.getOverview())
                .releaseDate(dto.getReleaseDate())
                .voteAverage(dto.getVoteAverage())
                .popularity(dto.getPopularity())
                .genres(genres)
                .actors(actors)
                .director(director)
                .build();

        movieDAO.save(movie);
    }


    public Set<Movie> getAllMovies() {
        return movieDAO.findAll().stream()
                .collect(Collectors.toSet());
    }


    public Set<Movie> searchMovies(String keyword) {
        return movieDAO.searchByTitle(keyword).stream()
                .collect(Collectors.toSet());
    }


    public Set<Movie> getTop10HighestRated() {
        return movieDAO.findAll().stream()
                .sorted((m1, m2) -> {
                    Double v1 = m1.getVoteAverage();
                    Double v2 = m2.getVoteAverage();
                    if (v1 == null && v2 == null) return 0;
                    if (v1 == null) return 1;
                    if (v2 == null) return -1;
                    return v2.compareTo(v1); // descending order
                })
                .limit(10)
                .collect(Collectors.toSet());
    }


    public Set<Movie> getTop10MostPopular() {
        return movieDAO.findAll().stream()
                .sorted((m1, m2) -> {
                    Double p1 = m1.getPopularity();
                    Double p2 = m2.getPopularity();
                    if (p1 == null && p2 == null) return 0;
                    if (p1 == null) return 1;
                    if (p2 == null) return -1;
                    return p2.compareTo(p1); // descending order
                })
                .limit(10)
                .collect(Collectors.toSet());
    }

    public Double getAverageRating() {
        return movieDAO.findAll().stream()
                .map(m -> m.getVoteAverage())
                .filter(v -> v != null)
                .mapToDouble(v -> v)
                .average()
                .orElse(0.0);
    }
}
