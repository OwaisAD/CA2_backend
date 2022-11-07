package facades;

import TestEnvironment.TestEnvironment;
import entities.User;
import errorhandling.IllegalAgeException;
import utils.EMF_Creator;
import entities.RenameMe;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class UserFacadeTest extends TestEnvironment {

    private static UserFacade facade;

    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        facade = UserFacade.getUserFacade(emf);
    }

    // TODO: Delete or change this method 
    @Test
    public void createUserTest() throws Exception {
        User user = createUser();

        assertNull(user.getId());

        user = facade.createUser(user);

        assertNotNull(user.getId());
    }

    @Test
    public void createUserWithAgeUnder13Test() {
        User user = createUser();
        user.setAge(12);
        assertThrows(IllegalAgeException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithAgeAbove120Test() {
        User user = createUser();
        user.setAge(121);
        assertThrows(IllegalAgeException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithAge13Test() {
        User user = createUser();
        user.setAge(13);
        assertDoesNotThrow(() -> facade.createUser(user));
    }

    @Test
    public void createUserWithAge120Test() {
        User user = createUser();
        user.setAge(120);
        assertDoesNotThrow(() -> facade.createUser(user));
    }

}
