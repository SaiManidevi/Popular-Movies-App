package com.example.android.knowmovies.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

/**
 * This ViewModel is used to perform the Insert and Delete operation
 * and is used in the DetailsActivity - the activity that
 * displays and stores a specific movie's details
 */
public class InsertViewModel extends AndroidViewModel {
    private FavMovieRepository mRepository;

    public InsertViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FavMovieRepository(application);
    }

    public void insert(Movie favMovie) {
        mRepository.insert(favMovie);
    }

    public void delete(Movie favMovie) {
        mRepository.delete(favMovie);
    }

}
