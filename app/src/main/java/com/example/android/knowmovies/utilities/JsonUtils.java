package com.example.android.knowmovies.utilities;

import android.content.Context;
import android.text.TextUtils;

import com.example.android.knowmovies.data.Movie;
import com.example.android.knowmovies.utilities.classes.Review;
import com.example.android.knowmovies.utilities.classes.Trailer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {
    /**This method is called from the MainActivity to parse the JSON Response
     * Parsing is achieved with the help of GSON library
     * @param JSONData - String of JSON Response received after making the network call
     * @return List<Movie> Returns a List of Movie objects
     */
    public static List<Movie> getMoviesStringFromJSON(String JSONData) throws JSONException {

        List<Movie> moviesList = new ArrayList<>();
        Gson gson = new Gson();

        JSONArray results = getResults(JSONData);
        if (results.length() > 0) {
                Type movieType = new TypeToken<ArrayList<Movie>>() {
                }.getType();
                moviesList = gson.fromJson(results.toString(), movieType);
            }
        return moviesList;
    }

    public static List<Trailer> getTrailerStringFromJson(Context context, String JSONData) throws JSONException {
        List<Trailer> trailerList = new ArrayList<>();
        Gson gson = new Gson();

        JSONArray results = getResults(JSONData);
        if (results.length() > 0) {
            Type trailerType = new TypeToken<ArrayList<Trailer>>() {
            }.getType();
            trailerList = gson.fromJson(results.toString(), trailerType);
        }
        return trailerList;
    }

    public static List<Review> getReviewStringFromJson(Context context, String JSONData) throws JSONException {

        List<Review> reviewList = new ArrayList<>();
        Gson gson = new Gson();

        JSONArray results = getResults(JSONData);
        if (results.length() > 0) {
            Type reviewType = new TypeToken<ArrayList<Review>>() {
            }.getType();
            reviewList = gson.fromJson(results.toString(), reviewType);
        }

        return reviewList;
    }

    /**
     * This is a helper method to reduce repeated code in the above three methods.
     * @param JSONData - The JSON data received from the Network API call (TMDb API)
     * @return null if @JSONData is empty else returns a JSONArray - 'results'
     * @throws JSONException
     */
    private static JSONArray getResults(String JSONData) throws JSONException {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JSONData)) {
            return null;
        }
        JSONArray results = null;
        try {
            JSONObject movieDBJson = new JSONObject(JSONData);
            results = movieDBJson.getJSONArray(MovieConstants.ARRAY_RESULTS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }
}
