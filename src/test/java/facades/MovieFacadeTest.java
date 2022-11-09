package facades;

import TestEnvironment.TestEnvironment;
import entities.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class MovieFacadeTest extends TestEnvironment {

    private static MovieFacade facade;


    @BeforeAll
    static void beforeAll() {
        TestEnvironment.setUpClass();
        facade = MovieFacade.getMovieFacade(emf);
    }

    @Test
    void addMovieToDataBaseTest() {
        Movie expected = createMovie();
        Movie actual = facade.createMovie(expected);
        assertEquals(expected.getYear(), actual.getYear());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertDatabaseHasEntity(actual, actual.getId());
    }

    @Test
    void removeMovieFromDatabaseTest() {
        Movie expected = createAndPersistMovie();
        facade.removeMovie(expected);
        assertDatabaseDoesNotHaveEntity(expected, expected.getId());
    }

    @Test
    void getMovieByTitleAndYearTest() {
        Movie expected = createAndPersistMovie();
        Movie actual = facade.getMovieByTitleAndYear(expected.getTitle(),expected.getYear());

        assertEquals(expected.getId(),actual.getId());
    }

    @Test
    void getNonExistingMovieByTitleAndYearTest() {
        Movie expected = createMovie();

        Movie actual = facade.getMovieByTitleAndYear(expected.getTitle(),expected.getYear());

        assertNull(actual);
    }

    @Test
    void getMovieByIdTest() {
        Movie expected = createAndPersistMovie();

        Movie actual = facade.getMovieById(expected.getId());

        assertEquals(expected.getId(),actual.getId());
    }

    @Test
    void getNonExistingMovieByIdTest() {
        Movie actual = facade.getMovieById(nonExistingId);

        assertNull(actual);
    }
}
