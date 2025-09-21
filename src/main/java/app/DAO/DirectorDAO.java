package app.DAO;

import app.entities.Actor;
import app.entities.Director;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

public class DirectorDAO extends GenericDAO<Director> {
    public DirectorDAO() {
        super(Director.class);
    }

    public DirectorDAO(EntityManagerFactory emf) {
        super(Director.class, emf);
    }

    public Director findByTmdbId(Long tmdbId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT d FROM Director d WHERE d.tmdbId = :tmdbId", Director.class)
                    .setParameter("tmdbId", tmdbId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
