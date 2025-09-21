package app.DAO;

import app.DAO.Populator.MoviePopulator;
import app.config.HibernateConfig;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;
class MovieDAOTest {
    private static EntityManagerFactory emf;
    private static MovieDAO dao;
    private static Movie m1;
    private static Movie m2;

    @BeforeAll
    static void setup() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new MovieDAO(emf);
    }

    @AfterAll
    static void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @BeforeEach
    void cleanDatabase() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Movie").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE movie_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();

            Movie[] testMovie = MoviePopulator.populate(dao);
            m1 = testMovie[0];
            m2 = testMovie[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void searchByTitle() {
        List<Movie> results = dao.searchByTitle("inception");

        assertThat(results, hasSize(1));

        Movie found = results.get(0);

        assertEquals(m2, found);

        assertThat(found, samePropertyValuesAs(m2, "id"));
    }

    @Test
    void findByTmdbId() {
        Movie found = dao.findByTmdbId(m1.getTmdbId());

        assertNotNull(found);

        assertEquals(m1, found);

        assertThat(found, samePropertyValuesAs(m1, "id"));

        Movie notFound = dao.findByTmdbId(9999999L);
        assertNull(notFound);
    }
}