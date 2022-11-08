package dtos;

import entities.Movie;

public class MovieDTO {

    private final String title;
    private final int year;

    public MovieDTO(Movie movie) {
        this.title = movie.getTitle();
        this.year = movie.getYear();
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }
}
