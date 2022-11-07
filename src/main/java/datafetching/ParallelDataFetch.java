package datafetching;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dtos.MovieDTO;
import dtos.MovieReviewCombinedDTO;
import dtos.ReviewDTO;
import utils.HttpUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelDataFetch {
    private static ExecutorService es = Executors.newCachedThreadPool();
    static String API_KEY_OMDB = "52e5ff12";
    static String API_KEY_NY = "5QjomAGUzfEYR3EdFfcVYCuHAYLAG0FK";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    //using threads based on: https://www.baeldung.com/java-future

    // add movie future
    public static Future<MovieDTO> getMovie(String movieName) {
        return es.submit(() -> {
            String movieJSON = HttpUtils.fetchData("https://www.omdbapi.com/?apikey=" + API_KEY_OMDB +"&t=" + movieName);
            MovieDTO movieDTO = GSON.fromJson(movieJSON, MovieDTO.class);
            movieDTO.setDataReference("https://omdbapi.com/");
            return movieDTO;
        });
    }

    // add review future
    public static Future<ReviewDTO> getReview(String movieName) {
        return es.submit(() -> {
            String reviewJSON = HttpUtils.fetchData("https://api.nytimes.com/svc/movies/v2/reviews/search.json?query=" + movieName + "&api-key=" + API_KEY_NY);
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
    public static MovieReviewCombinedDTO runParallel(String movieName) throws ExecutionException, InterruptedException {
        MovieDTO movieDTO = getMovie(movieName).get();
        ReviewDTO reviewDTO = getReview(movieName).get();
        MovieReviewCombinedDTO movieReviewCombinedDTO = new MovieReviewCombinedDTO(movieDTO, reviewDTO);
        return movieReviewCombinedDTO;
    }
}
