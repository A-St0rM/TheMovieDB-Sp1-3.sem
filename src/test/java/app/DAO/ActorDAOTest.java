package app.DAO;

import app.DAO.Populator.ActorPopulator;
import app.config.HibernateConfig;
import app.entities.Actor;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActorDAOTest {

    private EntityManagerFactory emf;
    private ActorDAO actorDAO;
    private Actor actor1;
    private Actor actor2;
    private Actor actor3;
    private List<Actor> testActors;

    @BeforeAll
    void initOnce() {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        actorDAO = new ActorDAO(emf);
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Actor").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE actor_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();

            Actor[] testActors = ActorPopulator.populate(actorDAO);
            actor1 = testActors[0];
            actor2 = testActors[1];
            actor3 = testActors[2];
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
        Actor newActor = Actor.builder().name("Diana Douglas").build();

        // Act
        Actor saved = actorDAO.save(newActor);

        // Assert
        assertNotNull(saved.getId(), "Saved actor should have an ID");
        assertEquals("Diana Douglas", saved.getName());

        // And the total count should be 4 (3 from populator + 1 new)
        List<Actor> all = actorDAO.findAll();
        assertEquals(4, all.size());
    }

    @Test
    void findById() {
        // Act
        Actor actor = actorDAO.findById(actor1.getId());
        // Assert
        assertNotNull(testActors);
        assertThat(actor, hasProperty("id", is(actor1.getId())));
        assertThat(actor, hasProperty("name", is(actor1.getName())));
        assertEquals(actor, actor1);
    }

    @Test
    void findAll() {
        // Act
        List<Actor> all = actorDAO.findAll();
        // Assert
        assertThat(all, is(notNullValue()));
        assertThat(all, hasSize(3));
        assertThat(all, everyItem(hasProperty("id", notNullValue())));
        assertThat(all, containsInAnyOrder(actor1, actor2, actor3));
    }

    @Test
    void update() {
        // Arrange
        String original = actor2.getName();
        String updatedName = original + " (updated)";
        Actor toUpdate = Actor.builder()
                .id(actor2.getId())   // ensure we update the existing row
                .name(updatedName)
                .build();

        // Act
        Actor merged = actorDAO.update(toUpdate);

        // Assert (returned object)
        assertThat(merged, hasProperty("id", is(actor2.getId())));
        assertThat(merged, hasProperty("name", is(updatedName)));

        // Assert (read-back consistency)
        Actor reloaded = actorDAO.findById(actor2.getId());
        assertNotNull(reloaded);
        assertThat(reloaded, hasProperty("name", is(updatedName)));

        // Count unchanged (still 3 from populator)
        assertEquals(3, actorDAO.findAll().size());
    }

    @Test
    void update_nonExisting_shouldNotCreateSilently() {
        // Arrange
        Actor phantom = Actor.builder().id(9999).name("Ghost").build();
        // Act + Assert
        assertThrows(ApiException.class, () -> actorDAO.update(phantom));
    }

    @Test
    void delete() {
        // Act
        actorDAO.delete(actor3.getId());

        // Assert: now actor3 should be gone
        Actor afterDelete = actorDAO.findById(actor3.getId());
        assertNull(afterDelete);

        // Count should drop to 2
        List<Actor> remaining = actorDAO.findAll();
        assertThat(remaining, hasSize(2));
        assertThat(remaining, containsInAnyOrder(actor1, actor2));
    }

    @Test
    void findByTmdbId() {
        // Act
        Actor actor = actorDAO.findByTmdbId(actor3.getId());
        // Assert
        assertEquals(actor, actor3);
    }
}