package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import entities.User;
import errorhandling.*;
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
    public Response createUser(String userFromJson) throws EntityNotFoundException, InvalidPasswordException, MissingDataException, IllegalAgeException {

        String errorMsg = "";
        int statusCode = 201;
        UserDTO userDTO = GSON.fromJson(userFromJson, UserDTO.class);
        User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getAge());

        try {
            user = facade.createUser(user);
        } catch (InvalidUsernameException e) {
            statusCode = 400;
            errorMsg = "Username length must be between "
                    + UserFacade.MINIMUM_USERNAME_LENGTH + " and "
                    + UserFacade.MAXIMUM_USERNAME_LENGTH +" characters long";
            ExceptionDTO exceptionDTO = new ExceptionDTO(statusCode, errorMsg);
            return Response.status(statusCode).entity(GSON.toJson(exceptionDTO)).build();
        }


        String result = GSON.toJson(new UserDTO(user.getId(), user.getUsername(), user.getAge()));
        return Response.status(statusCode).entity(result).build();
    }
}
