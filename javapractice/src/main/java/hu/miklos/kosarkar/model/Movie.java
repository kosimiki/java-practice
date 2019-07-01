package hu.miklos.kosarkar.model;

import java.time.LocalDate;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    private List<Genre> genres;
    private LocalDate releaseDate;

    public Movie(String title, String description, List<Genre> genres, LocalDate releaseDate) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
