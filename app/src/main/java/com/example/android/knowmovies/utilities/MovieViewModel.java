package com.example.android.knowmovies.utilities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.android.knowmovies.data.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    public List<Movie> movieList;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository();
    }

    public List<Movie> getMovieList(String sortChoice, int pageNumber) {
        movieList = movieRepository.getMovieList(sortChoice, pageNumber);
        return movieList;
    }
}
