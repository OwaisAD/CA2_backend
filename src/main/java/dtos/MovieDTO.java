package dtos;

import entities.Movie;

public class MovieDTO {

    private int id;
    private String title;
    private int year;

    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.year = movie.getYear();
    }

    public MovieDTO(String title, int year) {
        this.title = title;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }
}
