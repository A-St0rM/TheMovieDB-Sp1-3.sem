package app.service;

import app.DAO.GenreDAO;
import app.entities.Genre;
import app.entities.Movie;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenreService {
    private final GenreDAO genreDAO = new GenreDAO();

    public Set<Genre> getAllGenres() {
        return new HashSet<>(genreDAO.findAll());
    }

    public List<Movie> getMoviesByGenre(String name) {
        List<Movie> movies = genreDAO.findMoviesByGenre(name);
        return movies;
    }
}
