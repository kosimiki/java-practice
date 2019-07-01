package hu.miklos.kosarkar.service;

import hu.miklos.kosarkar.model.Genre;
import hu.miklos.kosarkar.model.Movie;
import hu.miklos.kosarkar.model.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class UserService {

    void addUser(User user){
        throw new NotImplementedException();

    }

    void addFavouriteMovie(String userName, Movie movie){
        throw new NotImplementedException();
    }

    void addMovie(String userName, Movie movie){
        throw new NotImplementedException();
    }

    void addPreferredGenre(String userName, Genre genre){
        throw new NotImplementedException();
    }

    void addBlacklistedGenre(String userName, Genre genre){
        throw new NotImplementedException();
    }

    /**
     *
     * @return the user or users with the most seen movies
     */
    List<User> getTheBiggestMovieFan(){
        throw new NotImplementedException();
    }

    public User getUserByUserName(String user) {
        throw new NotImplementedException();

    }
}
