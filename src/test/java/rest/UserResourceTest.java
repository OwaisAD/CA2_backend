package rest;

import dtos.MovieDTO;
import dtos.MovieDTOFromOMDB;
import dtos.UserDTO;
import entities.Movie;
import entities.User;
import entities.UserMovie;
import io.restassured.http.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserResourceTest extends ResourceTestEnvironment {

    private final String BASE_URL = "/users/";

    @Test
    void createUserTest() {
        UserDTO userDTO = createUserDTO();

        int id = given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED_201.getStatusCode())
                .contentType(ContentType.JSON)
                .body("username", equalTo(userDTO.getUsername()))
                .body("age", equalTo(userDTO.getAge()))
                .body("id", notNullValue())
                .body("roles",hasSize(1))
                .body("roles",hasItem("user"))
                .extract().path("id");

        assertDatabaseHasEntity(new User(), id);
    }

    @Test
    void createUserInvalidUsernameTest() {
        UserDTO userDTO = createUserDTO();
        userDTO.setUsername(faker.letterify("??"));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message", notNullValue());

    }

    @Test
    void createUserInvalidPasswordTest() {
        UserDTO userDTO = createUserDTO();
        userDTO.setPassword(faker.letterify("??"));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message",notNullValue());
    }

    @Test
    void createUserIllegalAgeTest() {
        UserDTO userDTO = createUserDTO();
        userDTO.setAge(faker.random().nextInt(121,300));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(userDTO))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .contentType(ContentType.JSON)
                .body("message",notNullValue());
    }

    @Test
    void addMovieToUserTest() {
        User user = createAndPersistUser();
        MovieDTO movieDTO = createMovieDTO();

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(movieDTO))
                .when()
                .put(BASE_URL+user.getId()+"/movies")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("id",equalTo(user.getId()))
                .body("movies",hasSize(1));

//        user = (User) getRefreshedEntity(user);
//        UserMovie userMovie = user.getUserMovies().get(0);
//        assertDatabaseHasEntity(userMovie,userMovie.getId());
    }

    @Test
    void addMovieThatAlreadyExistsTest() {
        Movie movie = createAndPersistMovie();
        MovieDTO movieDTO = new MovieDTO(movie.getTitle(), movie.getYear());

        User user = createAndPersistUser();

        int expectedId = movie.getId();
        int actualId = given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(movieDTO))
                .when()
                .put(BASE_URL+user.getId()+"/movies")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("movies",hasSize(1))
                .extract().path("movies.id[0]");

        assertEquals(expectedId,actualId);
    }

    @Test
    void removeMovieFromUserTest() {
        User user = createAndPersistUser();
        Movie movie = createAndPersistMovie();
        user.addMovie(movie);
        update(user);

        given()
            .header("Content-type", ContentType.JSON)
            .when()
            .put(BASE_URL+user.getId()+"/movies/"+movie.getId())
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200.getStatusCode())
            .contentType(ContentType.JSON)
            .body("movies", hasSize(0));
    }
}
