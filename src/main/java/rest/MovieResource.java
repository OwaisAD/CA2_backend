package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.MovieReviewCombinedDTO;
import facades.FacadeExample;
import parallel.MovieReviewDataFetcher;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Path("movie")
public class MovieResource {

    @GET
    @Path("{movieName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMovieDataSequential(@PathParam("movieName") String movieName) throws IOException {
        LocalTime begin = LocalTime.now();
        MovieReviewCombinedDTO combined = MovieReviewDataFetcher.runSequential(movieName);
        LocalTime end = LocalTime.now();

        long endTime = ChronoUnit.NANOS.between(begin, end);
        return Response.ok().entity(MovieReviewCombinedDTO.getMovieDataAsJSON("Sequential run", combined, endTime)).build();
    }

//    @GET
//    @Path("/parallel/{name}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public String getMovieData(@PathParam("name") String movieName) {
//
//    }
}
