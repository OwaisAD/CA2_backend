package rest;

import entities.User;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserResourceTest extends RestTestEnvironment {

    private final String BASE_URL = "/users/";

    @Test
    void createUserTest() {
        User user = createUser();

        int id = given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(user))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("username", equalTo(user.getUsername()))
                .body("age", equalTo(user.getAge()))
                .body("id", notNullValue())
                .extract().path("id");

        assertDatabaseHasEntity(user, id);
    }

    @Test
    void createUserInvalidUsernameTest() {
        User user = createUser();
        user.setUsername(faker.letterify("??"));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(GSON.toJson(user))
                .when()
                .post(BASE_URL)
                .then()
                .assertThat()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("message", notNullValue());
    }

}
