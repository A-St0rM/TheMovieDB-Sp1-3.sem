package app.DAO;

import app.DAO.Populator.GenrePopulator;
import app.config.HibernateConfig;
import app.entities.Genre;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreDAOTest {

    private EntityManagerFactory emf;
    private GenreDAO genreDAO;
    private Genre genre1;
    private Genre genre2;
    private Genre genre3;
    private List<Genre> testGenres;

    @BeforeAll
    void initOnce() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        genreDAO = new GenreDAO(emf);
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Genre").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE genre_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();

            Genre[] testGenres = GenrePopulator.populate(genreDAO);
            genre1 = testGenres[0];
            genre2 = testGenres[1];
            genre3 = testGenres[2];
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to alter tables", e);
        }
    }

    @AfterAll
    void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void save() {
        // Arrange
        Genre newGenre = Genre.builder().name("Diana Douglas").build();

        // Act
        Genre saved = genreDAO.save(newGenre);

        // Assert
        assertNotNull(saved.getId(), "Saved genre should have an ID");
        assertEquals("Diana Douglas", saved.getName());

        // And the total count should be 4 (3 from populator + 1 new)
        List<Genre> all = genreDAO.findAll();
        assertEquals(4, all.size());
    }

    @Test
    void findById() {
        // Act
        Genre genre = genreDAO.findById(genre1.getId());
        // Assert
        assertNotNull(testGenres);
        assertThat(genre, hasProperty("id", is(genre1.getId())));
        assertThat(genre, hasProperty("name", is(genre1.getName())));
        assertEquals(genre, genre1);
    }

    @Test
    void findAll() {
        // Act
        List<Genre> all = genreDAO.findAll();
        // Assert
        assertThat(all, is(notNullValue()));
        assertThat(all, hasSize(3));
        assertThat(all, everyItem(hasProperty("id", notNullValue())));
        assertThat(all, containsInAnyOrder(genre1, genre2, genre3));
    }

    @Test
    void update() {
        // Arrange
        String original = genre2.getName();
        String updatedName = original + " (updated)";
        Genre toUpdate = Genre.builder()
                .id(genre2.getId())   // ensure we update the existing row
                .name(updatedName)
                .build();

        // Act
        Genre merged = genreDAO.update(toUpdate);

        // Assert (returned object)
        assertThat(merged, hasProperty("id", is(genre2.getId())));
        assertThat(merged, hasProperty("name", is(updatedName)));

        // Assert (read-back consistency)
        Genre reloaded = genreDAO.findById(genre2.getId());
        assertNotNull(reloaded);
        assertThat(reloaded, hasProperty("name", is(updatedName)));

        // Count unchanged (still 3 from populator)
        assertEquals(3, genreDAO.findAll().size());
    }

    @Test
    void update_nonExisting_shouldNotCreateSilently() {
        // Arrange
        Genre phantom = Genre.builder().id(9999).name("Ghost").build();
        // Act + Assert
        assertThrows(ApiException.class, () -> genreDAO.update(phantom));
    }

    @Test
    void delete() {
        // Act
        genreDAO.delete(genre3.getId());

        // Assert: now genre3 should be gone
        Genre afterDelete = genreDAO.findById(genre3.getId());
        assertNull(afterDelete);

        // Count should drop to 2
        List<Genre> remaining = genreDAO.findAll();
        assertThat(remaining, hasSize(2));
        assertThat(remaining, containsInAnyOrder(genre1, genre2));
    }

    @Test
    void findByTmdbId() {
        // Act
        Genre genre = genreDAO.findByTmdbId((int) genre3.getId());
        // Assert
        assertEquals(genre, genre3);
    }
}