package TestEnvironment;

import com.github.javafaker.Faker;
import entities.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

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

            Role role = createRole();
            role.setRole("user");
            persist(role);
        } finally {
            em.close();
        }
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        faker = Faker.instance(new Locale("da-DK"));
    }


    protected Entity persist(Entity entity) {
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

    protected Entity update(Entity entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
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
        try {
            return new User(
                    faker.name().username(),
                    faker.internet().password(),
                    faker.number().numberBetween(13, 120)
            );
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return null;
    }

    protected Role createAndPersistRole() {
        Role role = createRole();
        return (Role) persist(role);
    }

    protected Role createRole() {
        return new Role(faker.letterify("????"));
    }

    protected Movie createAndPersistMovie(){

        Movie movie = createMovie();
        return (Movie) persist(movie);
    }

    protected Movie createMovie(){
        return new Movie(faker.book().title(), faker.number().numberBetween(1888, 2030));
    }

    protected void assertDatabaseHasEntity(Entity entity, int id) {
        EntityManager em = emf.createEntityManager();
        try {
            entity = em.find(entity.getClass(), id);
            assertNotNull(entity, "Entity: " + entity.getClass()+" with id: " + id + " does not exist..");
        } finally {
            em.close();
        }
    }

    protected void assertDatabaseDoesNotHaveEntity(Entity entity, int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Entity foundEntity = em.find(entity.getClass(), id);
            assertNull(foundEntity, "Entity: " + entity.getClass()+" with id: " + id + " does exist!");
        } finally {
            em.close();
        }
    }

    protected void assertDatabaseHasEntityWith(Entity persistedEntity, String property, int value) {
        assertDatabaseHasEntity(persistedEntity,persistedEntity.getId());

        assertDatabaseHas(persistedEntity,property,value);
    }

    protected void assertDatabaseHas(Entity persistedEntity, String property, int value) {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Entity> query = em.createQuery(
                "SELECT e FROM " + persistedEntity.getClass().getSimpleName()
                        + " e WHERE e." + property + " = :value " +
                        "AND e.id =:id",Entity.class);

        query.setParameter("value",value);
        query.setParameter("id",persistedEntity.getId());

        try {
            query.getSingleResult();
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false,persistedEntity.getClass().getSimpleName()+" does not have "+
                    property+" with value "+value+" in database");
        }
    }

    protected Entity getRefreshedEntity(Entity entity) {
        EntityManager em = emf.createEntityManager();
        try {
            entity = em.find(entity.getClass(), entity.getId());
            em.getTransaction().begin();
            em.merge(entity);
            em.refresh(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return entity;
    }
}
