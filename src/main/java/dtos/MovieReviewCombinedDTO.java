package dtos;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class MovieReviewCombinedDTOs {
    private String title;
    private String timeSpent;
    private MovieReviewCombinedDTO data;

    public MovieReviewCombinedDTOs(String title, long timeSpent, MovieReviewCombinedDTO data) {
        this.title = title;
        this.timeSpent = "" + ((timeSpent) / 1_000_000) + "ms.";
        this.data = data;
    }
}

public class MovieReviewCombinedDTO {
    private MovieDTOFromOMDB movie;
    private ReviewDTO review;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MovieReviewCombinedDTO(MovieDTOFromOMDB movie, ReviewDTO review) {
        this.movie = movie;
        this.review = review;
    }

    public static String getMovieDataAsJSON(String title, MovieReviewCombinedDTO movieData, long time) {
        return GSON.toJson(new MovieReviewCombinedDTOs(title, time, movieData));
    }

    public MovieDTOFromOMDB getMovie() {
        return movie;
    }

    public void setMovie(MovieDTOFromOMDB movie) {
        this.movie = movie;
    }

    public ReviewDTO getReview() {
        return review;
    }

    public void setReview(ReviewDTO review) {
        this.review = review;
    }
}
