package com.example.android.knowmovies.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * This ViewModel is used to perform the display of all movies
 * and is used in the FavoritesActivity - the Activity which displays
 * all the user's favorite movies stored in the database.
 */
public class FavMovieViewModel extends AndroidViewModel {

    private FavMovieRepository mRepository;
    private LiveData<List<Movie>> mAllFavMovies;

    public FavMovieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FavMovieRepository(application);
        mAllFavMovies = mRepository.getAllFavMovies();
    }

    public LiveData<List<Movie>> getAllFavMovies(){
        return mAllFavMovies;
    }
}
