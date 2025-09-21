package app.DAO;

import app.DAO.Populator.DirectorPopulator;
import app.config.HibernateConfig;
import app.entities.Director;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorDAOTest {

    private EntityManagerFactory emf;
    private DirectorDAO directorDAO;
    private Director director1;
    private Director director2;
    private Director director3;
    private List<Director> testDirectors;

    @BeforeAll
    void initOnce() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        directorDAO = new DirectorDAO(emf);
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Director").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE director_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();

            Director[] testDirectors = DirectorPopulator.populate(directorDAO);
            director1 = testDirectors[0];
            director2 = testDirectors[1];
            director3 = testDirectors[2];
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
        Director newDirector = Director.builder().name("Diana Douglas").build();

        // Act
        Director saved = directorDAO.save(newDirector);

        // Assert
        assertNotNull(saved.getId(), "Saved director should have an ID");
        assertEquals("Diana Douglas", saved.getName());

        // And the total count should be 4 (3 from populator + 1 new)
        List<Director> all = directorDAO.findAll();
        assertEquals(4, all.size());
    }

    @Test
    void findById() {
        // Act
        Director director = directorDAO.findById(director1.getId());
        // Assert
        assertNotNull(testDirectors);
        assertThat(director, hasProperty("id", is(director1.getId())));
        assertThat(director, hasProperty("name", is(director1.getName())));
        assertEquals(director, director1);
    }

    @Test
    void findAll() {
        // Act
        List<Director> all = directorDAO.findAll();
        // Assert
        assertThat(all, is(notNullValue()));
        assertThat(all, hasSize(3));
        assertThat(all, everyItem(hasProperty("id", notNullValue())));
        assertThat(all, containsInAnyOrder(director1, director2, director3));
    }

    @Test
    void update() {
        // Arrange
        String original = director2.getName();
        String updatedName = original + " (updated)";
        Director toUpdate = Director.builder()
                .id(director2.getId())   // ensure we update the existing row
                .name(updatedName)
                .build();

        // Act
        Director merged = directorDAO.update(toUpdate);

        // Assert (returned object)
        assertThat(merged, hasProperty("id", is(director2.getId())));
        assertThat(merged, hasProperty("name", is(updatedName)));

        // Assert (read-back consistency)
        Director reloaded = directorDAO.findById(director2.getId());
        assertNotNull(reloaded);
        assertThat(reloaded, hasProperty("name", is(updatedName)));

        // Count unchanged (still 3 from populator)
        assertEquals(3, directorDAO.findAll().size());
    }

    @Test
    void update_nonExisting_shouldNotCreateSilently() {
        // Arrange
        Director phantom = Director.builder().id(9999L).name("Ghost").build();
        // Act + Assert
        assertThrows(ApiException.class, () -> directorDAO.update(phantom));
    }

    @Test
    void delete() {
        // Act
        directorDAO.delete(director3.getId());

        // Assert: now director3 should be gone
        Director afterDelete = directorDAO.findById(director3.getId());
        assertNull(afterDelete);

        // Count should drop to 2
        List<Director> remaining = directorDAO.findAll();
        assertThat(remaining, hasSize(2));
        assertThat(remaining, containsInAnyOrder(director1, director2));
    }

    @Test
    void findByTmdbId() {
        // Act
        Director director = directorDAO.findByTmdbId(director3.getId());
        // Assert
        assertEquals(director, director3);
    }
}