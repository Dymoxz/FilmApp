package com.example.filmapp.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;
import com.example.filmapp.model.MovieReview;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewViewHolder> {

    private List<MovieReview> reviewList;

    public MovieReviewAdapter(List<MovieReview> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_overview_item_row, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        MovieReview review = reviewList.get(position);
        holder.authorTextView.setText(review.getAuthor());
        holder.dateTextView.setText(review.getDateCreated());
        holder.reviewTextView.setText(review.getReviewContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView authorTextView;
        TextView dateTextView;
        TextView reviewTextView;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.reviewListAuthor);
            dateTextView = itemView.findViewById(R.id.reviewListDate);
            reviewTextView = itemView.findViewById(R.id.reviewListReview);
        }
    }
}
