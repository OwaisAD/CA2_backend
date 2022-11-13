package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datafetching.ParallelDataFetch;
import dtos.*;
import entities.Movie;
import entities.User;
import errorhandling.*;
import facades.MovieFacade;
import facades.RoleFacade;
import facades.UserFacade;
import org.glassfish.grizzly.http.util.HttpStatus;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Path("users")
public class UserResource {

    @Context
    SecurityContext securityContext;

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
            facade.checkIfUserAlreadyExist(userDTO.getUsername());
            user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getAge());
            user.addRole(roleFacade.getRoleByRole("user"));
            user = facade.createUser(user);
        } catch (InvalidUsernameException | InvalidPasswordException | IllegalAgeException | MissingDataException |
                 UsernameAlreadyExists e) {
            throw new BadRequestException(e.getMessage());
        }

        userDTO = new UserDTO(user.getId(), user.getUsername(), user.getAge(),
                user.getRolesAsStringList(),user.getMovies());
        String userToJson = GSON.toJson(userDTO);
        return Response.status(HttpStatus.CREATED_201.getStatusCode()).entity(userToJson).build();
    }

    @POST
    @Path("me/movies")
    @RolesAllowed("user")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response addMovieToUser(String movieFromJson) {
        MovieDTO movieDTO = GSON.fromJson(movieFromJson, MovieDTO.class);
        User user;
        int id = Integer.parseInt(securityContext.getUserPrincipal().getName());
        try {
            user = facade.getUserById(id);
            Movie movie = movieFacade.getMovieByTitleAndYear(movieDTO.getTitle(), movieDTO.getYear());

            if (movie == null) {
                movie = movieFacade.createMovie(new Movie(movieDTO.getTitle(), movieDTO.getYear()));
                user.addMovie(movie);
            } else {
                // check if user already has the movie
                boolean userAlreadyHasMovie = facade.checkIfUserAlreadyHasMovie(user.getId(), movie.getId());
                if(!userAlreadyHasMovie) {
                    user.addMovie(movie);
                }
            }
            facade.updateUser(user);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No such user with id " + id + " exist");
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getAge(),
                user.getRolesAsStringList(), user.getMovies());

        String userToJson = GSON.toJson(userDTO);

        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(userToJson).build();
    }

    @DELETE
    @RolesAllowed("user")
    @Path("me/movies/{movieId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeMovieFromUser(@PathParam("movieId") int movieId) {
        User user;
        int id = Integer.parseInt(securityContext.getUserPrincipal().getName());
        try {
            Movie movie = movieFacade.getMovieById(movieId);
            user = facade.getUserById(id);
            user.removeMovie(movie);
            facade.updateUser(user);

            if (movie != null && movie.getUsers().size() == 0) {
                movieFacade.removeMovie(movie);
            }

        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No such user with id " + id + " exist");
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getAge(),
                user.getRolesAsStringList(), user.getMovies());

        String userToJson = GSON.toJson(userDTO);

        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(userToJson).build();
    }

    @GET
    @RolesAllowed({"user","admin"})
    @Path("me")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMe() {
        User user;
        int id = Integer.parseInt(securityContext.getUserPrincipal().getName());
        try {
            user = facade.getUserById(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No such user with id " + id + " exist");
        }
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getAge(),
                user.getRolesAsStringList(),
                user.getMovies()
        );

        String userToJson = GSON.toJson(userDTO);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(userToJson).build();
    }

    @GET
    @RolesAllowed("user")
    @Path("me/movies")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMovies() {
        User user;
        List<MovieReviewCombinedDTO> movieAndReviewDTOs = new ArrayList<>();
        int id = Integer.parseInt(securityContext.getUserPrincipal().getName());
        try {
            user = facade.getUserById(id);
            List<Movie> movies = user.getMovies();

            ExecutorService executorService = Executors.newCachedThreadPool();
            ParallelDataFetch fetcher = new ParallelDataFetch(executorService);

            List<List<Future>> futures = new ArrayList<>();

            for (Movie movie : movies) {
                List<Future> innerfutures = new ArrayList<>();
                Future<MovieDTO> futureMovieDTO = fetcher.getMovie(movie.getTitle(), movie.getYear(), movie.getId());
                Future<ReviewDTO> futureReviewDTO = fetcher.getReview(movie.getTitle(), movie.getYear());
                innerfutures.add(futureMovieDTO);
                innerfutures.add(futureReviewDTO);
                futures.add(innerfutures);
            }

            for (List<Future> future : futures) {
                MovieDTO movieDTO = (MovieDTO) future.get(0).get();
                ReviewDTO reviewDTO = (ReviewDTO) future.get(1).get();
                movieAndReviewDTOs.add(new MovieReviewCombinedDTO(movieDTO, reviewDTO));
            }

        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No such user with id " + id + " exist");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String movieAndReviewDTOsToJson = GSON.toJson(movieAndReviewDTOs);
        return Response.status(HttpStatus.OK_200.getStatusCode()).entity(movieAndReviewDTOsToJson).build();
    }
}
