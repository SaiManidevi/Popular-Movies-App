package com.example.android.knowmovies.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.knowmovies.R;
import com.example.android.knowmovies.adapters.MovieAdapter;
import com.example.android.knowmovies.data.FavMovieViewModel;
import com.example.android.knowmovies.databinding.ActivityFavoritesBinding;
import com.example.android.knowmovies.utilities.MovieConstants;
import com.example.android.knowmovies.data.Movie;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

    private ActivityFavoritesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites);

        final MovieAdapter adapter = new MovieAdapter(this);
        binding.recyclerviewFavorites.setAdapter(adapter);
        binding.recyclerviewFavorites.setLayoutManager(new GridLayoutManager(this, 2));

        FavMovieViewModel mFavMovieViewModel = ViewModelProviders.of(this).get(FavMovieViewModel.class);
        mFavMovieViewModel.getAllFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> favMovies) {
                adapter.setMoviePoster(favMovies);
            }
        });
        getSupportActionBar().setTitle(MovieConstants.FAVORITES_TITLE);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieItemClick(Movie clickedMovieData) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, clickedMovieData);
        startActivity(intent);
    }
}
