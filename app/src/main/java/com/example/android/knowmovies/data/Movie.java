package com.example.android.knowmovies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * This helper class is used for JSON de-serialization
 * which is achieved using the GSON Library and also as a
 * custom user-defined datatype to access the Movie data
 */
@Entity(tableName = "movies_table")
public class Movie implements Serializable {

    /**
     * vote_count : 1345
     * id : 351286
     * video : false
     * vote_average : 6.6
     * title : Jurassic World: Fallen Kingdom
     * popularity : 287.343825
     * poster_path : /c9XxwwhPHdaImA2f1WEfEsbhaFB.jpg
     * original_language : en
     * original_title : Jurassic World: Fallen Kingdom
     * genre_ids : [28,12,878]
     * backdrop_path : /gBmrsugfWpiXRh13Vo3j0WW55qD.jpg
     * adult : false
     * overview : A volcanic eruption threatens the remaining dinosaurs on the island of Isla Nublar...
     * release_date : 2018-06-06
     */
    @Ignore
    private int vote_count;
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;
    @Ignore
    private boolean video;
    @ColumnInfo(name = "vote_average")
    private double vote_average;
    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @Ignore
    private double popularity;
    @ColumnInfo(name = "poster_path")
    private String poster_path;
    @Ignore
    private String original_language;
    @Ignore
    private String original_title;
    @ColumnInfo(name = "backdrop_path")
    private String backdrop_path;
    @Ignore
    private boolean adult;

    @ColumnInfo(name = "overview")
    private String overview;
    @ColumnInfo(name = "release_date")
    private String release_date;
    @Ignore
    private List<Integer> genre_ids;

    @Ignore
    public Movie() {
    }

    public Movie(int id, double vote_average, @NonNull String title, String poster_path, @NonNull String backdrop_path, @NonNull String release_date, String overview) {
        this.id = id;
        this.vote_average = vote_average;
        this.title = title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.release_date = release_date;
        this.overview = overview;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }
}