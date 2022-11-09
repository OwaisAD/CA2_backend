package dtos;

// from omdb api

public class MovieDTOFromOMDB {
    private String Title;
    private String Year;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Poster;

    public MovieDTOFromOMDB(String title, String year, String released, String runtime, String genre, String poster) {
        this.Title = title;
        Year = year;
        Released = released;
        Runtime = runtime;
        Genre = genre;
        Poster = poster;
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

}
