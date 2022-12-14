package facades;

import TestEnvironment.TestEnvironment;
import entities.Movie;
import entities.User;
import entities.UserMovie;
import errorhandling.IllegalAgeException;
import errorhandling.InvalidUsernameException;

import errorhandling.MissingDataException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

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
        facade = UserFacade.getFacade(emf);
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
        assertThrows(MissingDataException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithAgeAboveMaximumTest() {
        User user = createUser();
        user.setAge(121);
        assertThrows(MissingDataException.class, () -> facade.createUser(user));
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
        assertThrows(MissingDataException.class, () -> facade.createUser(user));
    }

    @Test
    public void createUserWithUsernameLengthAboveMaximumLengthTest() {
        User user = createUser();
        user.setUsername(faker.letterify("?????????????????????"));
        assertThrows(MissingDataException.class, () -> facade.createUser(user));
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

    @Test
    public void updateUserTest() {
        User user = createAndPersistUser();
        user.setAge(faker.number().numberBetween(13,120));

        facade.updateUser(user);

        assertDatabaseHasEntityWith(user,"age",user.getAge());
    }

    @Test
    public void updateUserWithANewMovieTest() {
        User user = createAndPersistUser();
        Movie movie = createAndPersistMovie();

        user.addMovie(movie);
        facade.updateUser(user);
        user = facade.getUserById(user.getId());
        assertEquals(1,user.getUserMovies().size());

        UserMovie userMovie = user.getUserMovies().get(0);
        assertDatabaseHasEntity(userMovie,userMovie.getId()); //Check if the relation exists in database
    }

    @Test
    public void getUserByIdTest() {
        User expected = createAndPersistUser();

        User actual = facade.getUserById(expected.getId());

        assertEquals(expected.getId(),actual.getId());
    }

    @Test
    public void getUserByNonExistingIdTest() {
        assertThrows(EntityNotFoundException.class,()-> facade.getUserById(nonExistingId));
    }

}
