package rest;

import TestEnvironment.TestEnvironment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class RestTestEnvironment extends TestEnvironment {

    protected static final int SERVER_PORT = 7777;
    protected static final String SERVER_URL = "http://localhost/api";
    private static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();

    protected static HttpServer httpServer;

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static HttpServer startServer() {
        ResourceConfig resourceConfig = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
    }

    @BeforeAll
    public static void setUpClass() {
        TestEnvironment.setUpClass();
        EMF_Creator.startREST_TestWithDB();

        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    protected UserDTO createUserDTO() {
        User user = createUser();
        UserDTO userDTO = new UserDTO(user);
        userDTO.setPassword(faker.letterify("????"));

        return new UserDTO(user);
    }
}
