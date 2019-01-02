package com.example.android.knowmovies.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavMovieRepository {
    private FavMovieDao mFavMovieDao;
    private LiveData<List<Movie>> mAllFavMovies;

    public FavMovieRepository(Application application) {
        FavMovieRoomDatabase db = FavMovieRoomDatabase.getDatabase(application);
        mFavMovieDao = db.favMovieDao();
        mAllFavMovies = mFavMovieDao.getAllFavMovies();
    }

    LiveData<List<Movie>> getAllFavMovies() {
        return mAllFavMovies;
    }

    /**
     * To insert a movie in the database
     *
     * @param favMovie - the specific movie to be inserted in the database
     */
    public void insert(Movie favMovie) {
        new insertAsyncTask(mFavMovieDao).execute(favMovie);
    }

    // Using an AsyncTask to perform the Insert database operation
    // in the background thread
    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {
        private FavMovieDao mAsyncTaskDao;

        insertAsyncTask(FavMovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... favMovies) {
            mAsyncTaskDao.insert(favMovies[0]);
            return null;
        }
    }

    /**
     * To delete a movie in the database
     *
     * @param favMovie - the specific movie to be deleted in the database
     */
    public void delete(Movie favMovie) {
        new deleteAsyncTask(mFavMovieDao).execute(favMovie);
    }

    // Using an AsyncTask to perform the Delete database operation
    // in the background thread
    private static class deleteAsyncTask extends AsyncTask<Movie, Void, Void> {
        private FavMovieDao mAsyncTaskDao;

        deleteAsyncTask(FavMovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... favMovies) {
            mAsyncTaskDao.deleteMovie(favMovies[0]);
            return null;
        }
    }

}
