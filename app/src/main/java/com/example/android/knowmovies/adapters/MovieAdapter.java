package com.example.android.knowmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.knowmovies.R;
import com.example.android.knowmovies.data.Movie;
import com.example.android.knowmovies.utilities.MovieConstants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> mMoviePosters;
    /*
     * An on-click handler to make it easy for an Activity to interface with
     * the RecyclerView
     */
    final private MovieItemClickListener mOnClickListener;
    /**
     * The interface that receives onClick messages.
     */
    public interface MovieItemClickListener {
        void onMovieItemClick(Movie clickedMovieData);
    }

    public MovieAdapter(MovieItemClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (mMoviePosters == null)
            return 0;
        else return mMoviePosters.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;
        public final TextView mMovieTextView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.iv_movie_poster);
            mMovieTextView = itemView.findViewById(R.id.tv_movie_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie currentMovie = mMoviePosters.get(adapterPosition);
            mOnClickListener.onMovieItemClick(currentMovie);
        }
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        Movie individualMovie = mMoviePosters.get(position);
        String movieTitle = individualMovie.getTitle();
        String individualPosterURL = individualMovie.getPoster_path();

        holder.mMovieTextView.setText(movieTitle);
        if (TextUtils.isEmpty(individualPosterURL)) {
            holder.mMovieImageView.setImageResource(R.drawable.poster_none);
        } else {
            String posterStringURL = MovieConstants.BASE_IMAGE_URL + individualPosterURL;
            Picasso.get().load(posterStringURL).into(holder.mMovieImageView);
        }
    }

    public void setMoviePoster(List<Movie> moviePosters) {
        mMoviePosters = moviePosters;
        notifyDataSetChanged();
    }

    public void addMoreMovies(List<Movie> movies) {
        mMoviePosters.addAll(movies);
        notifyDataSetChanged();
    }
}
