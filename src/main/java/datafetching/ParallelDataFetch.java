package datafetching;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dtos.MovieDTO;
import dtos.MovieDTOFromOMDB;
import dtos.MovieReviewCombinedDTO;
import dtos.ReviewDTO;
import utils.HttpUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ParallelDataFetch {

    static String API_KEY_OMDB = "52e5ff12";
    static String API_KEY_NY = "5QjomAGUzfEYR3EdFfcVYCuHAYLAG0FK";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    //using threads based on: https://www.baeldung.com/java-future

    protected static ExecutorService executorService;
    public ParallelDataFetch(ExecutorService executorService) {
        this.executorService = executorService;
    }


    // THE FACT THAT WE ARE TAKING MOVIEID AS AN ARGUMENT IN THIS METHOD SHOULD 100% BE CHANGED :)
    // add movie future
    public static Future<MovieDTO> getMovie(String movieName, int year, int movieId) {
        return executorService.submit(() -> {
            String movieJSON = HttpUtils.fetchData("https://www.omdbapi.com/?apikey="
                    + API_KEY_OMDB +"&t=" + movieName +"&y=" + year);
            MovieDTOFromOMDB movieDTOFromOMDB = GSON.fromJson(movieJSON, MovieDTOFromOMDB.class);
            MovieDTO movieDTO = new MovieDTO(movieDTOFromOMDB);
            movieDTO.setId(movieId);
            movieDTO.setDataReference("https://omdbapi.com/");
            return movieDTO;
        });
    }

    // add review future
    public static Future<ReviewDTO> getReview(String movieName, int year) {
        return executorService.submit(() -> {
            int openingYearPlusOne = year + 1;
            String reviewJSON = HttpUtils.fetchData("https://api.nytimes.com/svc/movies/v2/reviews/search.json?query=" + movieName + "&opening-date=" + year + "-01-01:" + openingYearPlusOne + "-01-01&api-key=" + API_KEY_NY);
            //Change to DTO not JsonObject
            JsonObject reviewJSON2 = GSON.fromJson(reviewJSON, JsonObject.class);
            JsonArray jsonArray = reviewJSON2.getAsJsonArray("results");

            JsonObject propertiesJson = (JsonObject) jsonArray.get(0);
            String summary_short = propertiesJson.get("summary_short").getAsString();

            JsonObject linkObject = (JsonObject) propertiesJson.get("link");
            String suggested_link_text = linkObject.get("suggested_link_text").getAsString();
            String review_url = linkObject.get("url").getAsString();

            ReviewDTO reviewDTO = new ReviewDTO(summary_short, suggested_link_text, review_url);
            reviewDTO.setReviewReference("https://www.nytimes.com/");
            return reviewDTO;
        });
    }

    // method to get both
    public static MovieReviewCombinedDTO runParallel(String movieName, int year) throws ExecutionException, InterruptedException {
       //Start both tasks before waiting
        Future<MovieDTO> futureMovieDTO = getMovie(movieName, year, 0);
        Future<ReviewDTO> futureReviewDTO = getReview(movieName, year);
        MovieDTO movieDTO = futureMovieDTO.get();
        ReviewDTO reviewDTO = futureReviewDTO.get();
        MovieReviewCombinedDTO movieReviewCombinedDTO = new MovieReviewCombinedDTO(movieDTO, reviewDTO);
        return movieReviewCombinedDTO;
    }

}
