package app.DAO.Populator;

import app.DAO.DirectorDAO;
import app.entities.Director;

public class DirectorPopulator {
    public static Director[] populate(DirectorDAO directorDAO) {
        Director director1 =Director.builder()
                .tmdbId(1426672L)
                .name("Rasmus")
                .build();

        Director director2 = Director.builder()
                .tmdbId(176788346672L)
                .name("Alissa")
                .build();

        Director director3 = Director.builder()
                .tmdbId(909090L)
                .name("John")
                .build();

        directorDAO.save(director1);
        directorDAO.save(director2);
        directorDAO.save(director3);

        return new Director[]{director1, director2, director3};
    }
}