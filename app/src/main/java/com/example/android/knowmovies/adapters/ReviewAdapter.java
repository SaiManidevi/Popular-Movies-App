package com.example.android.knowmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.knowmovies.R;
import com.example.android.knowmovies.utilities.classes.Review;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private final String TAG = MovieAdapter.class.getSimpleName();
    private List<Review> mReviews;

    /**
     * The interface that receives onClick messages.
     */
    public interface ReviewItemClickListener {
        void onReviewItemClick(Review clickedReviewData);
    }

    /*
     * An on-click handler to make it easy for an Activity to interface with
     * the RecyclerView
     */
    final private ReviewItemClickListener mOnClickListener;

    public ReviewAdapter(ReviewItemClickListener listener) {
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        Review individualReview = mReviews.get(position);

        String reviewAuthor = individualReview.getAuthor().trim();
        String reviewContent = individualReview.getContent().trim();
        holder.mReviewAuthorTextView.setText(reviewAuthor);
        holder.mReviewContentTextView.setText(reviewContent);
    }

    @Override
    public int getItemCount() {
        if (mReviews == null)
            return 0;
        else return mReviews.size();
    }

    public void setReviewInfo(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mReviewAuthorTextView;
        public final TextView mReviewContentTextView;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mReviewAuthorTextView = itemView.findViewById(R.id.tv_review_author);
            mReviewContentTextView = itemView.findViewById(R.id.tv_review_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Review currentReview = mReviews.get(adapterPosition);
            mOnClickListener.onReviewItemClick(currentReview);
        }
    }
}
