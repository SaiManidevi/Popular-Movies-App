package com.example.android.knowmovies.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.android.knowmovies.R;
import com.example.android.knowmovies.adapters.MovieAdapter;
import com.example.android.knowmovies.utilities.MovieViewModel;
import com.example.android.knowmovies.databinding.ActivityMainBinding;
import com.example.android.knowmovies.utilities.EndlessRecyclerViewScrollListener;
import com.example.android.knowmovies.data.Movie;
import com.example.android.knowmovies.utilities.MovieConstants;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private static final String PREF_SORT_KEY = "choice_key";
    private static final String RECYCLERVIEW_STATE = "recycler_state";
    SharedPreferences sharedPref;
    private MovieAdapter mMovieAdapter;
    private String choiceOfSorting;
    private int pageNumber;
    GridLayoutManager gridLayoutManager;
    private MovieViewModel mViewModel;
    Parcelable savedRecyclerLayoutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //added DataBinding to reduce the amount of code
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //initialize page number (beginning from page 1)
        pageNumber = 1;
        //read the stored SharedPreferences if any, else assign the default sort - Popular
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String defaultSort = MovieConstants.POPULAR;
        choiceOfSorting = sharedPref.getString(PREF_SORT_KEY, defaultSort);
        setTitleText();
        //Initializing ViewModel to handle configuration changes
        mViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieAdapter = new MovieAdapter(this);
        //Setting up the GridLayout
        gridLayoutManager = new GridLayoutManager(
                this,
                calculateNoOfColumns(this));
        binding.recyclerviewMovies.setLayoutManager(gridLayoutManager);
        binding.recyclerviewMovies.setAdapter(mMovieAdapter);
        binding.recyclerviewMovies.setSaveEnabled(true);
        //Check for Internet Connection and then load data
        loadMovieData(false);
        //if users swipes-to-refresh, try to load the data by calling loadMovieData() method
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMovieData(true);
            }
        });
        //To achieve infinite scroll of movie data
        recyclerviewScrollListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savedRecyclerLayoutState = binding.recyclerviewMovies.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLERVIEW_STATE, savedRecyclerLayoutState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLERVIEW_STATE);
            binding.recyclerviewMovies.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    /**
     * Updates the RecyclerView with Movie data (if connected to the Network)
     *
     * @param fromSwipeRefreshLayout to ensure whether the call for this method has come from
     *                               SwipeRefreshLayout's onRefresh method so that the progress indicator
     *                               can be set to false.
     */
    private void loadMovieData(boolean fromSwipeRefreshLayout) {
        if (fromSwipeRefreshLayout) {
            // instructs the SwipeRefreshLayout to remove the progress indicator and update the view contents
            // (view contents can be either List of movies or No Internet available message)
            binding.swipeRefresh.setRefreshing(false);
        }
        if (connectedToNetwork()) {
            internetConnectionAvailable();
            //re-initialize page number (beginning from page 1)
            pageNumber = 1;
            mMovieAdapter.setMoviePoster(mViewModel.getMovieList(choiceOfSorting, pageNumber));
        } else noInternetConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuID = item.getItemId();
        //Three things to do when sort changes:
        //1. Update the variable - choiceOfSorting
        //2. Scroll back to the top when the sort changes
        //3. Change the ActionBar's title appropriately
        switch (menuID) {
            case R.id.top_rated_menu:
                choiceOfSorting = MovieConstants.TOP_RATED;
                gridLayoutManager.smoothScrollToPosition(binding.recyclerviewMovies,null,0);
                setTitleText();
                break;
            case R.id.now_playing_menu:
                choiceOfSorting = MovieConstants.NOW_PLAYING;
                gridLayoutManager.smoothScrollToPosition(binding.recyclerviewMovies,null,0);
                setTitleText();
                break;
            case R.id.most_popular_menu:
                choiceOfSorting = MovieConstants.POPULAR;
                gridLayoutManager.smoothScrollToPosition(binding.recyclerviewMovies,null,0);
                setTitleText();
                break;
            case R.id.favorites_menu:
                Intent intentFavMovies = new Intent(this, FavoritesActivity.class);
                startActivity(intentFavMovies);
                break;
        }
        //When sort changes, display movies from page 1
        pageNumber = 1;
        //store the sort choice with the help of SharedPreferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_SORT_KEY, choiceOfSorting);
        editor.apply();

        //After user selects one of the options from the settings menu,
        //make the network call ONLY if there is network connection
        //else display no network connection
        if (connectedToNetwork()) {
            internetConnectionAvailable();
            mMovieAdapter.setMoviePoster(mViewModel.getMovieList(choiceOfSorting, pageNumber));
        } else
            noInternetConnection();
        return super.onOptionsItemSelected(item);
    }

    /**
     * To handle the RecyclerView clicks
     *
     * @param clickedMovieData - A Movie object containing the data of the clicked Movie
     */
    @Override
    public void onMovieItemClick(Movie clickedMovieData) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, clickedMovieData);
        startActivity(intent);
    }

    /** HELPER METHODS **/

    /**
     * This method helps to achieve endless-scroll with the help of the
     * Abstract class - EndlessRecyclerViewScrollListener
     */
    private void recyclerviewScrollListener() {
        // Store a member variable for the listener
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (connectedToNetwork()) {
                    //first, save the current state of the scroll (for configuration changes)
                    savedRecyclerLayoutState = binding.recyclerviewMovies.getLayoutManager().onSaveInstanceState();
                    //increment the pageNumber to display the next page of movies
                    pageNumber = pageNumber + 1;
                    //Through the ViewModel make the network call
                    List<Movie> movieList = mViewModel.getMovieList(choiceOfSorting, pageNumber);
                    mMovieAdapter.addMoreMovies(movieList);
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.notConnectedToInternet), Toast.LENGTH_LONG).show();
            }
        };
        binding.recyclerviewMovies.addOnScrollListener(scrollListener);
    }

    /**
     * Helper method to dynamically calculate the no. of columns in GridLayout
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    private void setTitleText() {
        switch (choiceOfSorting) {
            case MovieConstants.POPULAR:
                getSupportActionBar().setTitle(MovieConstants.POPULAR_TITLE);
                break;
            case MovieConstants.TOP_RATED:
                getSupportActionBar().setTitle(MovieConstants.TOP_RATED_TITLE);
                break;
            case MovieConstants.NOW_PLAYING:
                getSupportActionBar().setTitle(MovieConstants.NOW_PLAYING_TITLE);
                break;
        }
    }

    private boolean connectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void noInternetConnection() {
        binding.recyclerviewMovies.setVisibility(View.GONE);
        binding.noInternetView.setVisibility(View.VISIBLE);
    }

    public void internetConnectionAvailable() {
        binding.noInternetView.setVisibility(View.GONE);
        binding.recyclerviewMovies.setVisibility(View.VISIBLE);
    }
}