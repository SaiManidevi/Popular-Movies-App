package com.example.android.knowmovies.utilities;


import com.example.android.knowmovies.BuildConfig;

public class MovieConstants {
    //TMDb Base URLs with API Key
    public static final String MOVIE_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.TMDbKey + "&language=en-US";
    public static final String MOVIE_NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.TMDbKey + "&language=en-US";
    public static final String MOVIE_TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + BuildConfig.TMDbKey + "&language=en-US";
    public static final String MOVIE_UPCOMING_URL = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.TMDbKey + "&language=en-US";

    public static final String PAGE_BASE = "&page=";

    public static final String TRAILER_YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    public static final String MOVIE_DETAIL_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String TRAILER_BASE_URL = "videos?api_key=" + BuildConfig.TMDbKey + "&language=en-US";
    public static final String REVIEW_BASE_URL = "reviews?api_key=" + BuildConfig.TMDbKey + "&language=en-US";
    // Base URL for images
    //has to be appended with the poster url retrieved from Json Response
    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    // Base URL for trailer thumbnail image
    //has to be appended with the key retrieved from Json Response
    public static final String BASE_THUMBNAIL_URL = "https://img.youtube.com/vi/";
    public static final String BASE_THUMBNAIL_RESOLUTION = "/0.jpg";

    public static final String ARRAY_RESULTS = "results";
    public static final String SORT_BY = "sort_by";

    public static String GET_SORT_BY = "";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String NOW_PLAYING = "now_playing";

    public static final String POPULAR_TITLE = "Popular Movies";
    public static final String TOP_RATED_TITLE = "Top Rated Movies";
    public static final String NOW_PLAYING_TITLE = "Now Playing Movies";
    public static final String FAVORITES_TITLE = "Your Favorite Movies";

}
