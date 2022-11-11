package dtos;

import entities.Movie;

public class MovieDTO {

    private Integer id;
    private String title;
    private Integer year;

    private String released;
    private String runtime;
    private String genre;
    private String poster;

    private String plot;
    private String actors;
    private String data_reference;

    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.year = movie.getYear();
    }

    public MovieDTO(String title, Integer year) {
        this.title = title;
        this.year = year;
    }

    public MovieDTO(MovieDTOFromOMDB movieDTOFromOMDB) {
        this.title = movieDTOFromOMDB.getTitle();
        this.year = Integer.parseInt(movieDTOFromOMDB.getYear());
        this.released = movieDTOFromOMDB.getReleased();
        this.runtime = movieDTOFromOMDB.getRuntime();
        this.genre = movieDTOFromOMDB.getGenre();
        this.poster = movieDTOFromOMDB.getPoster();
        this.plot = movieDTOFromOMDB.getPlot();
        this.actors = movieDTOFromOMDB.getActors();
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getDataReference() {
        return data_reference;
    }

    public void setDataReference(String dataReference) {
        this.data_reference = dataReference;
    }
}
