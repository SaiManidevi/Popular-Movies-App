package com.example.android.knowmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.knowmovies.R;
import com.example.android.knowmovies.utilities.MovieConstants;
import com.example.android.knowmovies.utilities.classes.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final String TAG = MovieAdapter.class.getSimpleName();
    private List<Trailer> mTrailers;
    /*
     * An on-click handler to make it easy for an Activity to interface with
     * the RecyclerView
     */
    final private TrailerItemClickListener mOnClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface TrailerItemClickListener {
        void onTrailerItemClick(Trailer clickedTrailerData);
    }

    public TrailerAdapter(TrailerItemClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder holder, int position) {
        Trailer individualTrailer = mTrailers.get(position);
        String trailerName = individualTrailer.getName();
        String trailerThumbnailKey = individualTrailer.getKey();
        String trailerThumbnailURL = MovieConstants.BASE_THUMBNAIL_URL + trailerThumbnailKey + MovieConstants.BASE_THUMBNAIL_RESOLUTION;

        holder.mTrailerTextView.setText(trailerName);
        Picasso.get().load(trailerThumbnailURL).into(holder.mTrailerImageView);
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null)
            return 0;
        else return mTrailers.size();
    }

    public void setTrailerInfo(List<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mTrailerImageView;
        public final TextView mTrailerTextView;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mTrailerImageView = itemView.findViewById(R.id.iv_trailerThumbnail);
            mTrailerTextView = itemView.findViewById(R.id.tv_trailerInfo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer currentTrailer = mTrailers.get(adapterPosition);
            mOnClickListener.onTrailerItemClick(currentTrailer);
        }
    }

}
