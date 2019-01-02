package com.example.android.knowmovies.utilities;

import android.os.AsyncTask;

import com.example.android.knowmovies.data.Movie;

import java.net.URL;
import java.util.List;

public class MovieRepository {
    private List<Movie> movieList;


    public List<Movie> getMovieList(String choiceOfSorting, int pageNumber) {
        try {
            movieList = new getMoviesAsyncTask(choiceOfSorting, pageNumber).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }

    //Performing the network calls in a background thread using AsyncTask
    public static class getMoviesAsyncTask extends AsyncTask<Void, Void, List<Movie>> {
        private String mSortChoice;
        private int mPageNumber;

        getMoviesAsyncTask(String sortChoice, int pageNumber) {
            mSortChoice = sortChoice;
            mPageNumber = pageNumber;
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {
            try {
                URL movieRequestURL = NetworkUtils.buildUrl(mSortChoice, mPageNumber);
                String JSONResponse = NetworkUtils.getResponseFromHttp(movieRequestURL);
                return JsonUtils.getMoviesStringFromJSON(JSONResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
        }
    }
}
