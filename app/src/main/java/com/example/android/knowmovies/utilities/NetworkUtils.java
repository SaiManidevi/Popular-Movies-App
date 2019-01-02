package com.example.android.knowmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    /**
     * This method is called from the MainActivity to build the URL based on the choice of sorting
     * (Top rated, Popular, Popular Kids) selected by the user
     *
     * @param choiceOfSorting - The user selected choice of sort order
     * @return the built Url of the type URL
     */
    public static URL buildUrl(String choiceOfSorting, int pageNumber) {
        String movieBaseURL;
        switch (choiceOfSorting) {
            case MovieConstants.POPULAR:
                movieBaseURL = MovieConstants.MOVIE_POPULAR_URL;
                break;
            case MovieConstants.NOW_PLAYING:
                movieBaseURL = MovieConstants.MOVIE_NOW_PLAYING_URL;
                break;
            case MovieConstants.TOP_RATED:
                movieBaseURL = MovieConstants.MOVIE_TOP_RATED_URL;
                break;
            default:
                movieBaseURL = MovieConstants.MOVIE_POPULAR_URL;
                break;
        }

        movieBaseURL = movieBaseURL + MovieConstants.PAGE_BASE + pageNumber;
        Uri builder = Uri.parse(movieBaseURL)
                .buildUpon()
                .build();
        URL url = null;

        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URL: " + url);
        return url;
    }

    public static List<URL> buildMovieDetailsURL(String movieID) {
        List<URL> urlList = new ArrayList<>();
        Uri builderForTrailer = Uri.parse(MovieConstants.MOVIE_DETAIL_BASE_URL)
                .buildUpon()
                .appendPath(movieID)
                .appendEncodedPath(MovieConstants.TRAILER_BASE_URL)
                .build();

        Uri builderForReview = Uri.parse(MovieConstants.MOVIE_DETAIL_BASE_URL)
                .buildUpon()
                .appendPath(movieID)
                .appendEncodedPath(MovieConstants.REVIEW_BASE_URL)
                .build();

        URL urlTrailer = null;
        URL urlReview = null;

        try {
            urlTrailer = new URL(builderForTrailer.toString());
            urlReview = new URL(builderForReview.toString());
            urlList.add(urlTrailer);
            urlList.add(urlReview);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built Trailer URL: " + urlTrailer);
        Log.v(TAG, "Built Review URL: " + urlReview);
        return urlList;
    }

    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner s = new Scanner(inputStream);
            s.useDelimiter("\\A");

            boolean hasInput = s.hasNext();
            if (hasInput) {
                return s.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }


}
