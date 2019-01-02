package com.example.android.knowmovies.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.android.knowmovies.R;
import com.example.android.knowmovies.adapters.ReviewAdapter;
import com.example.android.knowmovies.adapters.TrailerAdapter;
import com.example.android.knowmovies.data.FavMovieRoomDatabase;
import com.example.android.knowmovies.data.InsertViewModel;
import com.example.android.knowmovies.databinding.ActivityDetailsBinding;
import com.example.android.knowmovies.utilities.AppExecutors;
import com.example.android.knowmovies.data.Movie;
import com.example.android.knowmovies.utilities.MovieConstants;
import com.example.android.knowmovies.utilities.JsonUtils;
import com.example.android.knowmovies.utilities.classes.MovieDetails;
import com.example.android.knowmovies.utilities.NetworkUtils;
import com.example.android.knowmovies.utilities.classes.Review;
import com.example.android.knowmovies.utilities.classes.Trailer;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetails>,
        TrailerAdapter.TrailerItemClickListener,
        ReviewAdapter.ReviewItemClickListener {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private ActivityDetailsBinding binding;
    private Movie mSelectedMovie;
    private String mMovieImageURL;
    private String mMovieBGImageURL;
    public String mMovieTitle;
    private String mMovieDescription;
    private String mMovieDate;
    private Double mMovieRatings;
    public int mMovieId;
    private String mMovieIdAsString;

    private static final int LOADER_ID = 202;
    private static final int TRAILER_LIST_INDEX = 0;
    private static final int REVIEW_LIST_INDEX = 1;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private FavMovieRoomDatabase mDb;
    private InsertViewModel mViewModel;
    private Boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                mSelectedMovie = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);
                mMovieImageURL = mSelectedMovie.getPoster_path();
                mMovieBGImageURL = mSelectedMovie.getBackdrop_path();
                mMovieTitle = mSelectedMovie.getTitle();
                mMovieDescription = mSelectedMovie.getOverview();
                mMovieDate = mSelectedMovie.getRelease_date();
                mMovieRatings = mSelectedMovie.getVote_average();
                mMovieId = mSelectedMovie.getId();
                mMovieIdAsString = Integer.toString(mMovieId);
            }
        }
        if (TextUtils.isEmpty(mMovieImageURL)) {
            binding.ivDetailMoviePoster.setImageResource(R.drawable.poster_none);
        } else {
            String posterStringURL = MovieConstants.BASE_IMAGE_URL + mMovieImageURL;
            Picasso.get().load(posterStringURL).into(binding.ivDetailMoviePoster);
        }
        if (TextUtils.isEmpty(mMovieBGImageURL)) {
            binding.ivDetailMovieBg.setImageResource(R.drawable.poster_none);
        } else {
            String posterStringURL = MovieConstants.BASE_IMAGE_URL + mMovieBGImageURL;
            Picasso.get().load(posterStringURL).into(binding.ivDetailMovieBg);
        }

        setToolbarText();
        setToolbarColor();

        binding.tvDetailMovieTitle.setText(mMovieTitle);
        binding.tvDetailMovieDescription.setText(mMovieDescription);
        binding.tvDetailMovieDate.setText(mMovieDate);
        binding.tvDetailMovieRating.setText(mMovieRatings.toString());

        if (connectedToNetwork()) {
            // Trailers
            mTrailerAdapter = new TrailerAdapter(this);
            binding.recyclerviewTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerviewTrailers.setAdapter(mTrailerAdapter);
            // Reviews
            mReviewAdapter = new ReviewAdapter(this);
            binding.recyclerviewReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            binding.recyclerviewReviews.setAdapter(mReviewAdapter);
            // Initialize the AsyncTaskLoader to generate the Trailers and Reviews
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }
        mDb = FavMovieRoomDatabase.getDatabase(getApplicationContext());
        mViewModel = ViewModelProviders.of(this).get(InsertViewModel.class);
        //The below method is called to assign the FAB's color and
        // to initialize the boolean variable - isFavorite
        checkIfMovieIsFavorite();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean connectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method to assign the Toolbar's text based on the current movie.
     */
    private void setToolbarText() {
        binding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbar.setTitle(mMovieTitle);
                    isShow = true;
                } else if (isShow) {
                    binding.collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * This helper AsyncTask class helps to convert the Movie's poster image URL to Bitmap
     * (Conversion to Bitmap is done so that Palette colors can be used)
     */
    public static class getBitmapAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        private String mPosterURL;

        public getBitmapAsyncTask(String posterURL) {
            mPosterURL = posterURL;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL url = new URL(mPosterURL);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    /**
     * Helper method to assign the Toolbar's color using Palette
     */
    public void setToolbarColor() {
        Bitmap bitmap = null;
        final Window window = this.getWindow();
        String posterURL = MovieConstants.BASE_IMAGE_URL + "/" + mMovieBGImageURL;
        if (connectedToNetwork()) {
            try {
                bitmap = new getBitmapAsyncTask(posterURL).execute().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bitmap != null) {
            // Generate the palette and get the vibrant swatch
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@NonNull Palette palette) {
                    // Load default colors
                    int backgroundColor = ContextCompat.getColor(getBaseContext(),
                            R.color.default_title_background);
                    int statusBarColor = ContextCompat.getColor(getBaseContext(),
                            R.color.default_title_background);
                    int textColor = ContextCompat.getColor(getBaseContext(),
                            R.color.default_title_color);

                    Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                    //Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                    // Check that the Vibrant swatch is available
                    if (vibrantSwatch != null) {
                        statusBarColor = vibrantSwatch.getRgb();
                        backgroundColor = vibrantSwatch.getRgb();
                        textColor = vibrantSwatch.getTitleTextColor();
                    }
                    binding.collapsingToolbar.setStatusBarScrimColor(statusBarColor);
                    binding.collapsingToolbar.setContentScrimColor(backgroundColor);
                    binding.collapsingToolbar.setBackgroundColor(backgroundColor);
                    binding.collapsingToolbar.setCollapsedTitleTextColor(textColor);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        getWindow().setStatusBarColor(statusBarColor);
                    }
                }
            });
        } else {
            int backgroundColor = ContextCompat.getColor(getBaseContext(),
                    R.color.default_title_background);
            int statusBarColor = ContextCompat.getColor(getBaseContext(),
                    R.color.default_title_background);
            int textColor = ContextCompat.getColor(getBaseContext(),
                    R.color.default_title_color);

            binding.collapsingToolbar.setStatusBarScrimColor(statusBarColor);
            binding.collapsingToolbar.setContentScrimColor(backgroundColor);
            binding.collapsingToolbar.setCollapsedTitleTextColor(textColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setStatusBarColor(statusBarColor);
            }
        }
    }

    @NonNull
    @Override
    public Loader<MovieDetails> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<MovieDetails>(this) {
            MovieDetails movieDetails = null;

            @Override
            protected void onStartLoading() {
                if (movieDetails != null)
                    deliverResult(movieDetails);
                else
                    forceLoad();
            }

            @Nullable
            @Override
            public MovieDetails loadInBackground() {
                try {
                    List<URL> urlList = NetworkUtils.buildMovieDetailsURL(mMovieIdAsString);
                    URL trailerRequestURL = urlList.get(TRAILER_LIST_INDEX);
                    URL reviewRequestURL = urlList.get(REVIEW_LIST_INDEX);
                    String trailerJSONResponse = NetworkUtils.getResponseFromHttp(trailerRequestURL);
                    String reviewJSONResponse = NetworkUtils.getResponseFromHttp(reviewRequestURL);
                    List<Trailer> trailers = JsonUtils.getTrailerStringFromJson(DetailsActivity.this, trailerJSONResponse);
                    List<Review> reviews = JsonUtils.getReviewStringFromJson(DetailsActivity.this, reviewJSONResponse);
                    movieDetails = new MovieDetails(trailers, reviews);
                    return movieDetails;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable MovieDetails data) {
                movieDetails = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<MovieDetails> loader, MovieDetails data) {
        List<Trailer> trailerList = data.getTrailersList();
        List<Review> reviewList = data.getReviewsList();
        mTrailerAdapter.setTrailerInfo(trailerList);
        mReviewAdapter.setReviewInfo(reviewList);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<MovieDetails> loader) {

    }

    @Override
    public void onTrailerItemClick(Trailer clickedTrailerData) {
        String completeTrailerURL = MovieConstants.TRAILER_YOUTUBE_BASE_URL + clickedTrailerData.getKey();
        Uri trailerURL = Uri.parse(completeTrailerURL);
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerURL);
        startActivity(trailerIntent);
    }

    @Override
    public void onReviewItemClick(Review clickedReviewData) {
        Uri reviewURL = Uri.parse(clickedReviewData.getUrl());
        Intent reviewIntent = new Intent(Intent.ACTION_VIEW, reviewURL);
        startActivity(reviewIntent);
    }

    /**
     * This method is called when an Insert operation has to be performed.
     * The insertion is achieved with the help of the ViewModel class
     * named - InsertViewModel
     */
    public void insertMovie() {
        Movie favMovie = new Movie(mMovieId, mMovieRatings, mMovieTitle, mMovieImageURL, mMovieBGImageURL, mMovieDate, mMovieDescription);
        mViewModel.insert(favMovie);
        binding.fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_24dp));
        isFavorite = true;
    }

    /**
     * This method is called when a Delete operation has to be performed.
     * The deletion is achieved with the help of the ViewModel class
     * named - InsertViewModel
     */
    public void deleteMovie() {
        Movie favMovie = new Movie(mMovieId, mMovieRatings, mMovieTitle, mMovieImageURL, mMovieBGImageURL, mMovieDate, mMovieDescription);
        mViewModel.delete(favMovie);
        binding.fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
        isFavorite = false;
    }

    /**
     * This helper method checks to see if a specific movie is already
     * selected as Favorite by the user or not. This is achieved with
     * the help of the boolean variable - isFavorite
     */
    public void checkIfMovieIsFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String movieName = mDb.favMovieDao().checkIfMovieExists(mMovieId);
                // if the movie exists in the database, then it's name is assigned to the
                // String variable - movieName, if it doesn't then it is assigned as null
                if (movieName == null) {
                    isFavorite = false;
                    binding.fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_24dp));
                } else {
                    isFavorite = true;
                    binding.fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_24dp));
                }
            }
        });
    }

    /**
     * This method is called when the FAB button(@activity_details.xml -> R.id.fab) is clicked
     * With the help of the above method -> checkIfMovieIsFavorite() the boolean variable - isFavorite
     * is updated. This variable is used to determine whether INSERT or DELETE operation must take place.
     *
     * @param view
     */
    public void addMovieFavorite(View view) {
        String snackBarText;
        if (isFavorite) {
            deleteMovie();
            snackBarText = getString(R.string.removed_fav);
        } else {
            insertMovie();
            snackBarText = getString(R.string.added_fav);
        }
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), snackBarText, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.undo_string, new UndoListener());
        snackbar.show();
    }

    /**
     * This helper class is used to perform the Undo operation in the snackbar.
     */
    public class UndoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isFavorite)
                deleteMovie();
            else
                insertMovie();
        }
    }
}
