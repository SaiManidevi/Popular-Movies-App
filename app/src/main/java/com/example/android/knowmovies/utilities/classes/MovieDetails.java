package com.example.android.knowmovies.utilities.classes;

import java.util.List;

public class MovieDetails {
    List<Trailer> trailersList;
    List<Review> reviewsList;

    public MovieDetails(List<Trailer> trailersList, List<Review> reviewsList) {
        this.trailersList = trailersList;
        this.reviewsList = reviewsList;
    }

    public List<Trailer> getTrailersList() {
        return trailersList;
    }
    public List<Review> getReviewsList() {
        return reviewsList;
    }
}
