package datafetching;

import com.google.gson.*;
import dtos.MovieDTO;
import dtos.MovieDTOFromOMDB;
import dtos.MovieReviewCombinedDTO;
import dtos.ReviewDTO;
import io.github.cdimascio.dotenv.Dotenv;
import utils.HttpUtils;

import java.io.IOException;


public class SequentialDataFetch {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static String API_KEY_OMDB = "52e5ff12";
    static String API_KEY_NY = "98swTXWdB1GNFrsuJDTMqHV4uBU4wzul";

    public static MovieReviewCombinedDTO runSequential(String movieName) throws IOException, InterruptedException {

        // get movie data
        String movieJSON = HttpUtils.fetchData("https://www.omdbapi.com/?apikey=" + API_KEY_OMDB +"&t=" + movieName);
        MovieDTOFromOMDB movieDTOFromOMDB = GSON.fromJson(movieJSON, MovieDTOFromOMDB.class);
        MovieDTO movieDTO = new MovieDTO(movieDTOFromOMDB);
        movieDTO.setDataReference("https://omdbapi.com/");

        String openingYear = movieDTOFromOMDB.getYear();
        String openingYearPlusOne = String.valueOf(Integer.parseInt(openingYear) + 1);

        // get review data
        String reviewJSON = HttpUtils.fetchData("https://api.nytimes.com/svc/movies/v2/reviews/search.json?query=" + movieName + "&opening-date=" + openingYear + "-01-01:" + openingYearPlusOne + "-01-01&api-key=" + API_KEY_NY);
        JsonObject reviewJSON2 = GSON.fromJson(reviewJSON, JsonObject.class);
        JsonArray jsonArray = reviewJSON2.getAsJsonArray("results");

        JsonObject propertiesJson = (JsonObject) jsonArray.get(0);
        String summary_short = propertiesJson.get("summary_short").getAsString();

        JsonObject linkObject = (JsonObject) propertiesJson.get("link");
        String suggested_link_text = linkObject.get("suggested_link_text").getAsString();
        String review_url = linkObject.get("url").getAsString();

        ReviewDTO reviewDTO = new ReviewDTO(summary_short, suggested_link_text, review_url);
        reviewDTO.setReviewReference("https://www.nytimes.com/");

        MovieReviewCombinedDTO movieReviewCombinedDTO = new MovieReviewCombinedDTO(movieDTO, reviewDTO);
        return movieReviewCombinedDTO;
    }

}
