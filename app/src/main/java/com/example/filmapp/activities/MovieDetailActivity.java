package com.example.filmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.filmapp.R;
import com.example.filmapp.api.ApiInterface;
import com.example.filmapp.api.RetrofitClient;
import com.example.filmapp.api.response.VideoResponse;
import com.example.filmapp.application.repository.Repository;
import com.example.filmapp.application.repository.VideoRepository;
import com.example.filmapp.application.viewmodel.MovieViewModel;
import com.example.filmapp.application.viewmodel.VideoViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.Video;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity{

    private static final String API_KEY = "02ddd233c99c814bad1a7d4af98e681b";
    private Movie movie;
    private ApiInterface apiInterface;
    private VideoViewModel videoViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        VideoRepository videoRepository = new VideoRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).videoDao());
        videoViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(VideoViewModel.class);
        videoViewModel.init(videoRepository); // Corrected parameter name

        // Initialize the GenreViewModel

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_silhouette);

        }

        TextView titleTextView = findViewById(R.id.movieDetailTitle);
        TextView genreTextView = findViewById(R.id.movieDetailGenre);
        TextView releaseYearTextView = findViewById(R.id.movieDetailReleaseYear);
        TextView durationTextView = findViewById(R.id.movieDetailDuration);
        TextView ratingTextView = findViewById(R.id.movieDetailRating);
        TextView taglineTextView = findViewById(R.id.movieDetailtagline);
        TextView descriptionTextView = findViewById(R.id.movieDetailDescription);
        ListView castListView = findViewById(R.id.movieDetailCastList);
        ImageView imageView = findViewById(R.id.movieDetailImage);

        Intent intent = this.getIntent();

        Bundle bundle = intent.getExtras();
        if (bundle !=null) {
            movie = (Movie) bundle.getSerializable("value");
            Log.d("DetailActivity", "got movie " + movie.getTitle());

        }
        if (movie != null) {
            Log.d("DetailActivity", "Setting movie details to views: " + movie.getTitle());

            StringBuilder genreIdStringBuilder = new StringBuilder();
            for (int genreId : movie.getGenreIdList()) {
                genreIdStringBuilder.append(genreId).append(", ");
            }
            genreTextView.setText(genreIdStringBuilder);
            titleTextView.setText(movie.getTitle());
            releaseYearTextView.setText(movie.getReleaseDate());
            durationTextView.setText(String.valueOf(movie.getDuration()));
            ratingTextView.setText(String.valueOf(movie.getRating()));
            taglineTextView.setText(movie.getTagline());
            descriptionTextView.setText(movie.getDescription());
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getImagePath()).into(imageView);
        } else {
            Log.e("DetailActivity", "Movie object is null");
        }

        // Start review overview activity
        findViewById(R.id.openReviewOverviewButton).setOnClickListener(v -> {
            int movieId = (movie.getId());
            // Putting movieId in intent in order to fetch the right reviews
            Log.d("MovieDetailActivity", "Movie ID: " + movieId);
            Intent intentReview = new Intent(this, ReviewOverviewActivity.class);
            intentReview.putExtra("MOVIE_ID", movieId);
            startActivity(intentReview);
        });

        getMovieTrailer(movie.getId(), videoViewModel);

    }

    private void getMovieTrailer(int movieId, VideoViewModel videoViewModel) {
        // Make the API call
        Call<VideoResponse> call = apiInterface.getVideos(movieId, API_KEY, "en-US");
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()) {
                    // Handle successful response here
                    VideoResponse videoResponse = response.body();
                    Video trailer = videoResponse.getTrailer();
                    if (trailer != null) {
                        String trailerUrl = trailer.getUrl();
                        if (trailerUrl != null) {
                            trailer.setMovieId(movieId);
                            Log.d("Trailer link", trailerUrl);
                            Log.d("MovieDetailActivity", "MovieId:" + trailer.getMovieId());

                            videoViewModel.insertVideo(trailer);

                            // Code for embedding video
                             WebView webView = findViewById(R.id.trailerWebView);
                             String embedLink = "<iframe width=\"560\" height=\"315\" src=\"" + trailerUrl + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                             webView.loadData(embedLink, "text/html", "utf-8");
                             webView.getSettings().setJavaScriptEnabled(true);
                             webView.setWebChromeClient(new WebChromeClient());

                        } else {
                            Log.e("Trailer link", "Trailer URL is null");
                        }
                    } else {
                        Log.e("Trailer link", "Trailer object is null");
                    }
                } else {
                    // Handle error response
                    Log.e("API Error", "Failed to fetch movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                // Handle failure
                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
            }
        });
    }



}
