package TestEnvironment;

import com.github.javafaker.Faker;
import entities.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestEnvironment {

    protected int nonExistingId;
    protected static Faker faker;
    protected static EntityManagerFactory emf;

    @BeforeEach
    void setup() {
        nonExistingId = faker.random().nextInt(-100, 0);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM UserMovie").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createQuery("DELETE FROM Movie").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        faker = Faker.instance(new Locale("da-DK"));
    }


    private Entity persist(Entity entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return entity;
    }

    protected User createAndPersistUser() {
        User user = createUser();
        return (User) persist(user);
    }

    protected User createUser() {
        return new User(
                faker.name().username(),
                faker.internet().password(),
                faker.number().numberBetween(13, 115)
        );
    }

    protected void assertDatabaseHasEntity(Entity entity, int id) {
        EntityManager em = emf.createEntityManager();
        try {
            entity = em.find(entity.getClass(), id);
            assertNotNull(entity, "Entity: " + entity.getClass()+" with id: " + id + "does not exist..");
        } finally {
            em.close();
        }
    }

    protected void assertDatabaseDoesNotHaveEntity(Entity entity, int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Entity foundEntity = em.find(entity.getClass(), id);
            assertNull(foundEntity, "Entity: " + entity.getClass()+" with id: " + id + "does exist!");
        } finally {
            em.close();
        }
    }
}
