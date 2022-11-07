package facades;

import TestEnvironment.TestEnvironment;
import entities.User;
import errorhandling.IllegalAgeException;
import errorhandling.InvalidUsernameException;

import org.junit.jupiter.api.BeforeAll;
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
    public void createUserWithAgeBelowMinimumTest() {
        User user = createUser();
        user.setAge(12);
        assertThrows(IllegalAgeException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithAgeAboveMaximumTest() {
        User user = createUser();
        user.setAge(121);
        assertThrows(IllegalAgeException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithExactlyTheMinimumAgeTest() {
        User user = createUser();
        user.setAge(13);
        assertDoesNotThrow(() -> facade.createUser(user));
    }

    @Test
    public void createUserWithExactlyTheMaximumAgeTest() {
        User user = createUser();
        user.setAge(120);
        assertDoesNotThrow(() -> facade.createUser(user));
    }

    @Test
    public void createUserWithNullUsernameTest() {
        User user = createUser();
        user.setUsername(null);
        assertThrows(InvalidUsernameException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithEmptyUsernameTest() {
        User user = createUser();
        user.setUsername("");
        assertThrows(InvalidUsernameException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameLengthBelowMinimumLengthTest() {
        User user = createUser();
        user.setUsername(faker.letterify("??")); //two random characters
        assertThrows(InvalidUsernameException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameLengthAboveMaximumLengthTest() {
        User user = createUser();
        user.setUsername(faker.letterify("?????????????????????"));
        assertThrows(InvalidUsernameException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameLengthExactlyMinimumLengthTest() {
        User user = createUser();
        user.setUsername(faker.letterify("???"));
        assertDoesNotThrow(() -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameLengthExactlyMaximumLengthTest() {
        User user = createUser();
        user.setUsername(faker.letterify("????????????????????"));
        assertDoesNotThrow(() -> facade.createUser(user));
    }

    @Test
    public void createUserWhenUserIsNullTest() {
        assertThrows(NullPointerException.class, () -> facade.createUser(null));
    }

}
