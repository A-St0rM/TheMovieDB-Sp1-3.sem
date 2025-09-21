package app.DAO.Populator;

import app.DAO.ActorDAO;
import app.entities.Actor;

public class ActorPopulator {
    public static Actor[] populate(ActorDAO actorDAO) {
        Actor actor1 =Actor.builder()
                .tmdbId(1426672L)
                .name("Rasmus")
                .build();

        Actor actor2 = Actor.builder()
                .tmdbId(176788346672L)
                .name("Alissa")
                .build();

        Actor actor3 = Actor.builder()
                .tmdbId(909090L)
                .name("John")
                .build();

        actorDAO.save(actor1);
        actorDAO.save(actor2);
        actorDAO.save(actor3);

        return new Actor[]{actor1, actor2, actor3};
    }
}
