package dtos;

// from omdb api

public class MovieDTOFromOMDB {
    private String Title;
    private String Year;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Poster;
    private String Plot;
    private String Actors;

    public MovieDTOFromOMDB(String title, String year, String released, String runtime, String genre, String poster, String plot, String actors) {
        this.Title = title;
        this.Year = year;
        this.Released = released;
        this.Runtime = runtime;
        this.Genre = genre;
        this.Poster = poster;
        this.Plot = plot;
        this.Actors = actors;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }
}
