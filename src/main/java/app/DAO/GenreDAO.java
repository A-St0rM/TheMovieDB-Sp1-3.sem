package app.DAO;

import app.entities.Genre;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Set;

public class GenreDAO extends GenericDAO<Genre> {
    public GenreDAO() {
        super(Genre.class);
    }

    public Genre findByName(String name) {
        var em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT g FROM Genre g WHERE g.name = :name", Genre.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Movie> findMoviesByGenre(String genreName) {
        var em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM Movie m JOIN m.genres g WHERE g.name = :name", Movie.class)
                    .setParameter("name", genreName)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Genre findByTmdbId(int tmdbId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT g FROM Genre g WHERE g.tmdbId = :tmdbId", Genre.class)
                    .setParameter("tmdbId", tmdbId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

}
