package com.example.android.knowmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavMovieDao {
    //Insert a single movie
    @Insert
    void insert(Movie favMovie);

    //Delete a single movie
    @Delete
    void deleteMovie(Movie favMovie);

    //Delete all movies
    @Query("DELETE FROM movies_table")
    void deleteAll();

    //Display all movies
    @Query("SELECT * FROM movies_table")
    LiveData<List<Movie>> getAllFavMovies();

    //Get the movie's name based on the movie ID
    @Query("SELECT title FROM movies_table WHERE id LIKE :movieId")
    String checkIfMovieExists(int movieId);
}
