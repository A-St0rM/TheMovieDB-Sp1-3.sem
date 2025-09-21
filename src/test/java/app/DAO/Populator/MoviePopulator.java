package app.DAO.Populator;

import app.DAO.MovieDAO;
import app.entities.Movie;


public class MoviePopulator {
    public static Movie[] populate(MovieDAO movieDAO) {
        Movie m1 =Movie.builder()
                .tmdbId(1426672L)
                .title("Alissas adventure")
                .overview("Long time ago there was a string girl named Alissa")
                .releaseDate("2025-7-9")
                .voteAverage(5.7)
                .popularity(10.0)
                .build();

        Movie m2 = Movie.builder()
                .title("Inception")
                .tmdbId(176788346672L)
                .overview("In the galaxy far awayyy")
                .releaseDate("2024-13-5")
                .voteAverage(2.7)
                .popularity(6.0)
                .build();

        movieDAO.save(m2);
        movieDAO.save(m1);

        return new Movie[]{m1, m2};
    }
}
