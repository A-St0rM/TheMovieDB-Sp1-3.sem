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
            em.createNativeQuery("ALTER SEQUENCE parcel_id_seq RESTART WITH 1").executeUpdate();
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
    }

    @Test
    void findByTmdbId() {
    }
}