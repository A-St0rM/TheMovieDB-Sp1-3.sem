package app.DAO;

import app.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

public class ActorDAO extends GenericDAO<Actor> {
    public ActorDAO() {
        super(Actor.class);
    }

    //For test
    public ActorDAO(EntityManagerFactory emf) {
        super(Actor.class, emf);
    }

    public Actor findByTmdbId(Long tmdbId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Actor a WHERE a.tmdbId = :tmdbId", Actor.class)
                    .setParameter("tmdbId", tmdbId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
