package rest;

import dtos.MovieDTO;
import dtos.UserDTO;
import entities.Movie;
import entities.User;
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
        login(user.getUsername(), user.getUnhashedPassword());
        int movieId = given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(GSON.toJson(movieDTO))
                .when()
                .post(BASE_URL+"me/movies")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("id",equalTo(user.getId()))
                .body("movies",hasSize(1))
                .extract().path("movies.id[0]");

        assertDatabaseHasUserMovieRelation(user.getId(), movieId);
    }

    @Test
    void addMovieThatAlreadyExistsTest() {
        Movie movie = createAndPersistMovie();
        MovieDTO movieDTO = new MovieDTO(movie.getTitle(), movie.getYear());
        User user = createAndPersistUser();

        login(user.getUsername(), user.getUnhashedPassword());
        int expectedId = movie.getId();
        int actualId = given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(GSON.toJson(movieDTO))
                .when()
                .post(BASE_URL+"me/movies")
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
        login(user.getUsername(), user.getUnhashedPassword());
        given()
            .header("Content-type", ContentType.JSON)
            .header("x-access-token", securityToken)
            .when()
            .delete(BASE_URL+"me/movies/"+movie.getId())
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200.getStatusCode())
            .contentType(ContentType.JSON)
            .body("movies", hasSize(0));

        assertDatabaseDoesNotHaveUserMovieRelation(user.getId(), movie.getId());
    }

    @Test
    void removeMovieThatIsAlsoRelatedToOtherUsersTest() {
        Movie movie = createAndPersistMovie();

        User userNotDeleting = createAndPersistUser();
        userNotDeleting.addMovie(movie);
        update(userNotDeleting);

        User userDeleting = createAndPersistUser();
        userDeleting.addMovie(movie);
        update(userDeleting);

        login(userDeleting.getUsername(), userDeleting.getUnhashedPassword());
        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete(BASE_URL+"me/movies/"+movie.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("movies", hasSize(0));

        assertDatabaseHasEntity(movie,movie.getId());
        assertDatabaseHasUserMovieRelation(userNotDeleting.getId(),movie.getId());
        assertDatabaseDoesNotHaveUserMovieRelation(userDeleting.getId(), movie.getId());
    }

    @Test
    void removeMovieWithLastUserRelationTest() {
        User user = createAndPersistUser();
        Movie movie = createAndPersistMovie();
        user.addMovie(movie);
        update(user);

        login(user.getUsername(), user.getUnhashedPassword());
        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete(BASE_URL+"me/movies/"+movie.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());

        assertDatabaseDoesNotHaveEntity(movie,movie.getId());
    }

    @Test
    void removeNonExistingMovieFromUserTest() {
        User user = createAndPersistUser();

        login(user.getUsername(), user.getUnhashedPassword());
        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete(BASE_URL+"me/movies/"+nonExistingId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .contentType(ContentType.JSON)
                .body("movies", hasSize(0));
    }

    @Test
    void getUserTest() {
        User user = createAndPersistUser();

        int id = user.getId();
        login(user.getUsername(), user.getUnhashedPassword());
        given()
            .header("Content-type", ContentType.JSON)
            .header("x-access-token", securityToken)
            .when()
            .get(BASE_URL+"me")
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200.getStatusCode())
            .contentType(ContentType.JSON)
            .body("username", equalTo(user.getUsername()))
            .body("age", equalTo(user.getAge()))
            .body("id", equalTo(id))
            .body("roles",hasSize(1))
            .body("roles",hasItem("user"));


    }
}
