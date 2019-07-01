package hu.miklos.kosarkar.service;

import hu.miklos.kosarkar.exception.ConflictingPreferenceException;
import hu.miklos.kosarkar.model.Genre;
import hu.miklos.kosarkar.model.Movie;
import hu.miklos.kosarkar.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static hu.miklos.kosarkar.model.Genre.*;
import static org.junit.jupiter.api.Assertions.*;

class MovieDatabaseServiceTest {

    private UserService userService;
    private MovieDatabaseService movieService;


    @BeforeEach
    void setUp() {
        userService = new UserService();
        movieService = new MovieDatabaseService(userService);
        userService.addUser(new User("Miki"));


        movieService.addMovie(new Movie("Ghost in the shell", "Best anime movie ever", Arrays.asList(ACTION, SCI_FI), LocalDate.of(1999, 1, 2)));
        movieService.addMovie(new Movie("Aliens", "Get away from her BITCH", Arrays.asList(HORROR, SCI_FI, ACTION), LocalDate.of(1987, 12, 24)));
        movieService.addMovie(new Movie("Terminator 7", null, Arrays.asList(SCI_FI, ACTION), null));
        movieService.addMovie(new Movie("Shrek 2", "Amazing story of an Ogre and his family", Arrays.asList(ROMANCE, FANTASY, COMEDY), LocalDate.of(2009, 1, 23)));
        movieService.addMovie(new Movie("Shrek", "Shrek begins", Arrays.asList(FANTASY, ACTION, COMEDY), LocalDate.of(2000, 1, 23)));
        movieService.addMovie(new Movie("The lord of the rings", "Best fantasy movie ever", Arrays.asList(FANTASY, ROMANCE), LocalDate.of(1999, 6, 14)));
        movieService.addMovie(new Movie("Konosuba", "KAZUMA-SAMAAAAA!!!!!", Arrays.asList(ACTION, FANTASY, COMEDY), LocalDate.of(2017, 12, 25)));
        movieService.addMovie(new Movie("Spice and wolf", "Romance between a god and a human", Arrays.asList(ROMANCE), LocalDate.of(2009, 3, 4)));



    }

    @Test
    void searchMoviesShouldReturnMultipleMovies() {
        List<Movie> movieList = movieService.searchMoviesByTitle("Shrek");
        assertEquals(2, movieList.size());
    }

    @Test
    void searchMoviesShouldReturnNoMovies() {
        List<Movie> movieList = movieService.searchMoviesByTitle("No movie should match this");
        assertTrue(movieList.isEmpty());
    }


    @Test
    void searchMoviesShouldReturnAllMovies() {
        List<Movie> movieList = movieService.searchMoviesByTitle("");
        assertEquals(movieService.databaseSize(), movieList.size());
    }

    @Test
    void searchMoviesByDescription() {
        List<Movie> movieList = movieService.searchMoviesByDescription("kazuma");
        assertEquals(1, movieList.size());
    }

    @Test
    void conflictingPreferenceWhenAddingTheSameToBlacklist() {
        userService.addPreferredGenre("Miki", SCI_FI);
        assertThrows(ConflictingPreferenceException.class, () -> userService.addBlacklistedGenre("Miki", SCI_FI));
    }


    @Test
    void conflictingPreferenceWhenAddingTheSameToPreferred() {
        userService.addUser(new User("Miki"));
        userService.addBlacklistedGenre("Miki", ACTION);
        assertThrows(ConflictingPreferenceException.class, () -> userService.addPreferredGenre("Miki", ACTION)
        );
    }

    @Test
    void getPreferredMoviesSciFiButNotHorror() {
        userService.addBlacklistedGenre("Miki", SCI_FI);
        userService.addPreferredGenre("Miki", HORROR);
        List<Movie> movies = movieService.searchMoviesByPreference(userService.getUserByUserName("Miki"));
        assertEquals(2, movies.size());
        assertTrue(movies.contains(movieService.searchMoviesByTitle("Terminator 7").get(0)));
        assertTrue(movies.contains(movieService.searchMoviesByTitle("Ghost").get(0)));

    }

    @Test
    void getPreferredMoviesSciFiButNotAction() {
        userService.addBlacklistedGenre("Miki", SCI_FI);
        userService.addPreferredGenre("Miki", ACTION);
        List<Movie> movies = movieService.searchMoviesByPreference(userService.getUserByUserName("Miki"));
        assertEquals(0, movies.size());
    }

    @Test
    void getPreferredMoviesFantasyAndRomanceButNotAction() {
        userService.addBlacklistedGenre("Miki", FANTASY);
        userService.addBlacklistedGenre("Miki", ROMANCE);
        userService.addPreferredGenre("Miki", ACTION);
        List<Movie> movies = movieService.searchMoviesByPreference(userService.getUserByUserName("Miki"));
        assertEquals(3, movies.size());
        assertTrue(movies.contains(movieService.searchMoviesByTitle("Shrek 2").get(0)));
        assertTrue(movies.contains(movieService.searchMoviesByTitle("Spice").get(0)));
        assertTrue(movies.contains(movieService.searchMoviesByTitle("Lord").get(0)));
    }

    @Test
    void getMostWatchedMovieOfTheYear() {
        userService.addUser(new User("Andris"));
        userService.addMovie("Andris", movieService.searchMoviesByTitle("Ghost").get(0));
        userService.addMovie("Miki", movieService.searchMoviesByTitle("Ghost").get(0));
        userService.addMovie("Miki", movieService.searchMoviesByTitle("Lord").get(0));
        userService.addMovie("Miki", movieService.searchMoviesByTitle("Spice").get(0));
        userService.addMovie("Andris", movieService.searchMoviesByTitle("Shrek 2").get(0));
        userService.addMovie("Andris", movieService.searchMoviesByTitle("Konosuba").get(0));

        List<Movie> mostWatched99 = movieService.getMostWatchedMovieOfTheYear(1999);
        assertEquals(1, mostWatched99.size());
        assertEquals(movieService.searchMoviesByTitle("Ghost").get(0), mostWatched99.get(0));

        List<Movie> mostWatched04 = movieService.getMostWatchedMovieOfTheYear(2004);
        assertEquals(2, mostWatched99.size());
        assertTrue(mostWatched04.contains(movieService.searchMoviesByTitle("Spice").get(0)));
        assertTrue(mostWatched04.contains(movieService.searchMoviesByTitle("Shrek 2").get(0)));

        List<Movie> mostWatched17 = movieService.getMostWatchedMovieOfTheYear(1999);
        assertEquals(1, mostWatched17.size());
        assertEquals(movieService.searchMoviesByTitle("Konosuba").get(0), mostWatched17.get(0));

        List<Movie> mostWatched98 = movieService.getMostWatchedMovieOfTheYear(1998);
        assertEquals(0, mostWatched98.size());

    }

    @Test
    void getMostInfluentialMoviesByGenre() {
        userService.addUser(new User("Andris"));
        Movie ghostInTheShell = movieService.searchMoviesByTitle("Ghost").get(0);
        Movie spiceAndWolf = movieService.searchMoviesByTitle("Spice").get(0);
        Movie shrek2 =movieService.searchMoviesByTitle("Shrek 2").get(0);
        Movie terminator = movieService.searchMoviesByTitle("Terminator 7").get(0);
        Movie konosuba = movieService.searchMoviesByTitle("Konosuba").get(0);
        userService.addFavouriteMovie("Andris", ghostInTheShell);
        userService.addFavouriteMovie("Andris", terminator);
        userService.addFavouriteMovie("Miki", ghostInTheShell);
        userService.addFavouriteMovie("Miki", spiceAndWolf);
        userService.addFavouriteMovie("Andris", shrek2);
        userService.addFavouriteMovie("Andris", konosuba);

        Map<Genre, List<Movie>> mostInfluentialMoviesByGenre = movieService.getMostInfluentialMoviesByGenre();

        assertEquals(1, mostInfluentialMoviesByGenre.get(SCI_FI).size());
        assertTrue(mostInfluentialMoviesByGenre.get(SCI_FI).contains(ghostInTheShell));

        assertEquals(2, mostInfluentialMoviesByGenre.get(ROMANCE).size());
        assertTrue(mostInfluentialMoviesByGenre.get(ROMANCE).contains(spiceAndWolf));
        assertTrue(mostInfluentialMoviesByGenre.get(ROMANCE).contains(shrek2));

        assertEquals(1, mostInfluentialMoviesByGenre.get(COMEDY).size());
        assertTrue(mostInfluentialMoviesByGenre.get(ROMANCE).contains(konosuba));

        assertEquals(0, mostInfluentialMoviesByGenre.get(DOCUMENTARY).size());
    }

    @Test
    void getFirstMoveOfTheGenre(){
        Movie spiceAndWolf = movieService.searchMoviesByTitle("Spice").get(0);
        Movie shrek2 =movieService.searchMoviesByTitle("Shrek 2").get(0);

        assertEquals(0, movieService.getTheFirstMoviesOfTheGenre(DOCUMENTARY).size());

        assertEquals(1, movieService.getTheFirstMoviesOfTheGenre(SCI_FI).size());
        assertTrue( movieService.getTheFirstMoviesOfTheGenre(SCI_FI).get(0).getTitle().contains("Aliens"));

        assertEquals(2, movieService.getTheFirstMoviesOfTheGenre(ROMANCE).size());
        assertTrue( movieService.getTheFirstMoviesOfTheGenre(ROMANCE).contains(shrek2));
        assertTrue( movieService.getTheFirstMoviesOfTheGenre(ROMANCE).contains(spiceAndWolf));

    }

    @Test
    void getSizeOfDatabase() {
        movieService = new MovieDatabaseService(userService);
        assertEquals(0, movieService.databaseSize());
        movieService.addMovie(new Movie("Test", "", Collections.emptyList(), null));
        assertEquals(1, movieService.databaseSize());
    }
}