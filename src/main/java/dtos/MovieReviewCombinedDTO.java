package dtos;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class MovieReviewCombinedDTO {
    private MovieDTO movie;
    private ReviewDTO review;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MovieReviewCombinedDTO(MovieDTO movie, ReviewDTO review) {
        this.movie = movie;
        this.review = review;
    }

    public static String getMovieDataAsJSON(MovieReviewCombinedDTO combined) {
        return GSON.toJson(combined);
    }

    public MovieDTO getMovie() {
        return movie;
    }

    public void setMovie(MovieDTO movie) {
        this.movie = movie;
    }

    public ReviewDTO getReview() {
        return review;
    }

    public void setReview(ReviewDTO review) {
        this.review = review;
    }
}
