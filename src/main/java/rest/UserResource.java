package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.MovieDTO;
import dtos.UserDTO;
import entities.Movie;
import entities.User;
import errorhandling.IllegalAgeException;
import errorhandling.InvalidPasswordException;
import errorhandling.InvalidUsernameException;
import facades.MovieFacade;
import facades.RoleFacade;
import facades.UserFacade;
import org.glassfish.grizzly.http.util.HttpStatus;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class UserResource {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    private UserFacade facade = UserFacade.getFacade(emf);
    private RoleFacade roleFacade = RoleFacade.getFacade(emf);
    private MovieFacade movieFacade = MovieFacade.getFacade(emf);

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
        } catch (InvalidUsernameException | InvalidPasswordException | IllegalAgeException e) {
            throw new BadRequestException(e.getMessage());
        }

        userDTO = new UserDTO(user.getId(), user.getUsername(), user.getAge(),
                user.getRolesAsStrings(),user.getMovies());
        String userToJson = GSON.toJson(userDTO);
        return Response.status(HttpStatus.CREATED_201.getStatusCode()).entity(userToJson).build();
    }

    @POST
    @Path("{id}/movies")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addMovieToUser(@PathParam("id") int id, String movieFromJson) {
        MovieDTO movieDTO = GSON.fromJson(movieFromJson, MovieDTO.class);
        User user;

        try {
            Movie movie = movieFacade.getMovieByTitleAndYear(movieDTO.getTitle(), movieDTO.getYear());
            user = facade.getUserById(id);

            if (movie == null) {
                movie = movieFacade.createMovie(new Movie(movieDTO.getTitle(), movieDTO.getYear()));
            }

            user.addMovie(movie);
            facade.updateUser(user);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No such user with id " + id + " exist");
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getAge(),
                user.getRolesAsStrings(), user.getMovies());

        String userToJson = GSON.toJson(userDTO);

        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(userToJson).build();
    }

    @DELETE
    @Path("{id}/movies/{movieId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeMovieFromUser(@PathParam("id") int id, @PathParam("movieId") int movieId) {
        User user;

        try {
            Movie movie = movieFacade.getMovieById(movieId);
            user = facade.getUserById(id);

            user.removeMovie(movie);
            facade.updateUser(user);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No such user with id " + id + " exist");
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getAge(),
                user.getRolesAsStrings(), user.getMovies());

        String userToJson = GSON.toJson(userDTO);

        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(userToJson).build();
    }


}
