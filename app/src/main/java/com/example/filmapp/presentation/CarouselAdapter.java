//package com.example.filmapp.presentation;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.filmapp.R;
//import com.example.filmapp.model.Movie;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {
//
//    private List<Movie> movies;
//
//    public CarouselAdapter(List<Movie> movies) {
//        this.movies = movies;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_layout, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Movie movie = movies.get(position);
//        // Load trailer URL into WebView
//        holder.trailerWebView.loadUrl(movie.getTrailerUrl());
//        // Load image into ImageView
//        Picasso.get().load(movie.getImageUrl()).into(holder.movieDetailImage);
//    }
//
//    @Override
//    public int getItemCount() {
//        return movies.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        WebView trailerWebView;
//        ImageView movieDetailImage;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            trailerWebView = itemView.findViewById(R.id.trailerWebView);
//            movieDetailImage = itemView.findViewById(R.id.movieDetailImage);
//        }
//    }
//}
//
