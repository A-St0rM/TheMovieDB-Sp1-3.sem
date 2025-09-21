package app.DAO.Populator;

import app.DAO.GenreDAO;
import app.entities.Genre;

public class GenrePopulator {
    public static Genre[] populate(GenreDAO genreDAO) {
        Genre genre1 =Genre.builder()
                .tmdbId(1)
                .name("Rasmus")
                .build();

        Genre genre2 = Genre.builder()
                .tmdbId(2)
                .name("Alissa")
                .build();

        Genre genre3 = Genre.builder()
                .tmdbId(3)
                .name("John")
                .build();

        genreDAO.save(genre1);
        genreDAO.save(genre2);
        genreDAO.save(genre3);

        return new Genre[]{genre1, genre2, genre3};
    }
}