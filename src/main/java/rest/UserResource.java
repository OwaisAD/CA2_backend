package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import entities.User;
import errorhandling.IllegalAgeException;
import errorhandling.InvalidUsernameException;
import errorhandling.MissingDataException;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("users")
public class UserResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    private UserFacade facade = UserFacade.getUserFacade(emf);

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createUser(String userFromJson) throws EntityNotFoundException, InvalidUsernameException, MissingDataException, IllegalAgeException {

        String errorMsg = "";

        // Make person from request body
        User user = GSON.fromJson(userFromJson, User.class);
        System.out.println(userFromJson);

        user = facade.createUser(user);

        UserDTO userDTO = new UserDTO(user);

        String result = GSON.toJson(userDTO);
        return Response.status(201).entity(result).build();
    }
}
