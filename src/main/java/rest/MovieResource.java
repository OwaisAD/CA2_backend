package rest;

import datafetching.ParallelDataFetch;
import dtos.MovieReviewCombinedDTO;
import datafetching.SequentialDataFetch;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

@Path("movie")
public class MovieResource {

    @GET
    @Path("/seq/{movieName}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMovieDataSequential(@PathParam("movieName") String movieName) throws IOException {
        LocalTime begin = LocalTime.now();
        MovieReviewCombinedDTO combined = SequentialDataFetch.runSequential(movieName);
        LocalTime end = LocalTime.now();

        long endTime = ChronoUnit.NANOS.between(begin, end);
        return Response.ok().entity(MovieReviewCombinedDTO.getMovieDataAsJSON("Sequential run", combined, endTime)).build();
    }

    @GET
    @Path("/par/{name}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMovieData(@PathParam("name") String movieName) throws ExecutionException, InterruptedException {
        LocalTime begin = LocalTime.now();
        MovieReviewCombinedDTO combined = ParallelDataFetch.runParallel(movieName);
        LocalTime end = LocalTime.now();

        long endTime = ChronoUnit.NANOS.between(begin, end);
        return Response.ok().entity(MovieReviewCombinedDTO.getMovieDataAsJSON("Parallel run", combined, endTime)).build();
    }
}
