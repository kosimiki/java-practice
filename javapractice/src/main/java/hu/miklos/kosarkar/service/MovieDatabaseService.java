package hu.miklos.kosarkar.service;

import hu.miklos.kosarkar.model.Genre;
import hu.miklos.kosarkar.model.Movie;
import hu.miklos.kosarkar.model.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

public class MovieDatabaseService {

    private final UserService userService;

    public MovieDatabaseService(UserService userService) {
        this.userService = userService;
    }

    void addMovie(Movie movie) {
        throw new NotImplementedException();
    }

    /**
     * Return movies which title contains the title parameter string.
     *
     * @param title search string
     * @return list of movies
     */
    public List<Movie> searchMoviesByTitle(String title) {
        throw new NotImplementedException();
    }

    /**
     * Return movies which title description the description parameter string.
     *
     * @param description search string
     * @return list of movies
     */
    public List<Movie> searchMoviesByDescription(String description) {
        throw new NotImplementedException();
    }

    /**
     * Return a list of movies based on the user`s preference.
     *
     * @param user the movie should match at least one of the preferred genres,
     *             the movie should not match any of black listed genres
     * @return the list of movies that match the criteria
     */
    public List<Movie> searchMoviesByPreference(User user) {
        throw new NotImplementedException();
    }

    /**
     * @param year
     * @return the most watched movie of the year.
     */
    public List<Movie> getMostWatchedMovieOfTheYear(int year) {
        throw new NotImplementedException();
    }

    /**
     * @return a list of movies with the most favourites among users by genre
     */
    public Map<Genre, List<Movie>> getMostInfluentialMoviesByGenre() {
        throw new NotImplementedException();
    }

    public List<Movie> getTheFirstMoviesOfTheGenre(Genre genre) {
        throw new NotImplementedException();
    }

    public int databaseSize() {
        return 0;
    }
}
