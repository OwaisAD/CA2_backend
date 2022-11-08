package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import entities.User;
import errorhandling.*;
import facades.RoleFacade;
import facades.UserFacade;
import org.glassfish.grizzly.http.util.HttpStatus;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("users")
public class UserResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    private UserFacade facade = UserFacade.getUserFacade(emf);
    private RoleFacade roleFacade = RoleFacade.getRoleFacade(emf);

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createUser(String userFromJson) {

        UserDTO userDTO = GSON.fromJson(userFromJson, UserDTO.class);
        User user;

        try {
            user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getAge());
            user.addRole(roleFacade.getRoleByRole("user"));
            user = facade.createUser(user);
        } catch (InvalidUsernameException | InvalidPasswordException | IllegalAgeException  e) {
            throw new BadRequestException(e.getMessage());
        }

        userDTO = new UserDTO(user.getId(), user.getUsername(), user.getAge(),user.getRolesAsStrings());
        String userToJson = GSON.toJson(userDTO);
        return Response.status(HttpStatus.CREATED_201.getStatusCode()).entity(userToJson).build();
    }
}
