package app.DAO;

import app.entities.Genre;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;


public class MovieDAO extends GenericDAO<Movie> {

    public MovieDAO() {
        super(Movie.class);
    }

    public List<Movie> searchByTitle(String keyword) {
        var em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(:keyword)", Movie.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }


    public Movie findByTmdbId(Long tmdbId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Movie m WHERE m.tmdbId = :tmdbId", Movie.class)
                    .setParameter("tmdbId", tmdbId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
