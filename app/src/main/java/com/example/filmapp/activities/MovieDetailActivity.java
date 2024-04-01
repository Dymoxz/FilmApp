package com.example.filmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmapp.R;
import com.example.filmapp.api.ApiInterface;
import com.example.filmapp.api.RetrofitClient;
import com.example.filmapp.api.response.VideoResponse;
import com.example.filmapp.application.repository.Repository;
import com.example.filmapp.application.repository.VideoRepository;
import com.example.filmapp.application.viewmodel.MovieViewModel;
import com.example.filmapp.application.viewmodel.VideoViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.model.MediaItem;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.Video;
import com.example.filmapp.presentation.CarouselAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String API_KEY = "02ddd233c99c814bad1a7d4af98e681b";
    private Movie movie;
    private ApiInterface apiInterface;
    private VideoViewModel videoViewModel;
    private RecyclerView carouselRecyclerView;
    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        VideoRepository videoRepository = new VideoRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).videoDao());
        videoViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(VideoViewModel.class);
        videoViewModel.init(videoRepository);

        Repository repository = new Repository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieDao());
        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieViewModel.class);
        movieViewModel.init(repository);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_silhouette);
        }

        carouselRecyclerView = findViewById(R.id.carouselRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        carouselRecyclerView.setLayoutManager(layoutManager);

        // Initialize views
        TextView titleTextView = findViewById(R.id.movieDetailTitle);
        TextView genreTextView = findViewById(R.id.movieDetailGenre);
        TextView releaseYearTextView = findViewById(R.id.movieDetailReleaseYear);
        TextView durationTextView = findViewById(R.id.movieDetailDuration);
        TextView ratingTextView = findViewById(R.id.movieDetailRating);
        TextView taglineTextView = findViewById(R.id.movieDetailtagline);
        TextView descriptionTextView = findViewById(R.id.movieDetailDescription);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
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
        } else {
            Log.e("DetailActivity", "Movie object is null");
        }



        movieViewModel.getImagePath(movie.getId()).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String imagePath) {
                Log.d("MovieDetailActivity", "Image path: " + imagePath);
                // Retrieve the imagePath and pass it to the getMovieTrailer method
                getMovieTrailer(movie.getId(), imagePath);
            }
        });
        findViewById(R.id.reviewContainer).setOnClickListener(v -> {
            int movieId = (movie.getId());
            // Putting movieId in intent in order to fetch the right reviews
            Log.d("MovieDetailActivity", "Movie ID: " + movieId);
            Intent intentReview = new Intent(this, ReviewOverviewActivity.class);
            intentReview.putExtra("MOVIE_ID", movieId);
            startActivity(intentReview);
        });
    }

    private void getMovieTrailer(int movieId, String imagePath) {
        Call<VideoResponse> call = apiInterface.getVideos(movieId, API_KEY, "en-US");
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()) {
                    VideoResponse videoResponse = response.body();
                    Video trailer = videoResponse.getTrailer();
                    if (trailer != null) {
                        String trailerUrl = trailer.getUrl();
                        if (trailerUrl != null) {
                            trailer.setMovieId(movieId);
                            videoViewModel.insertVideo(trailer);
                            WebView webView = new WebView(getApplicationContext());
                            String embedLink = "<iframe width=\"100%\" height=\"315\" src=\"" + trailerUrl + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
                            webView.loadData(embedLink, "text/html", "utf-8");
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.setWebChromeClient(new WebChromeClient());
                            // Add video WebView to carousel
                            addMediaToCarousel(webView, imagePath);
                        } else {
                            Log.e("Trailer link", "Trailer URL is null");
                        }
                    } else {
                        Log.e("Trailer link", "Trailer object is null");
                    }
                } else {
                    Log.e("API Error", "Failed to fetch movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
            }
        });
    }

    private void addMediaToCarousel(WebView webView, String imagePath) {
        List<MediaItem> mediaItems = new ArrayList<>();
        // Add movie poster to the carousel
        ImageView posterImageView = new ImageView(this);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + imagePath).into(posterImageView);
        mediaItems.add(new MediaItem(imagePath, null)); // Pass imagePath here
        // Extract URL from WebView and add it to MediaItem
        String webViewUrl = webView.getUrl();
        if (webViewUrl != null) {
            mediaItems.add(new MediaItem(null, webViewUrl));
        }
        // Add other media items (images) as needed
        // ...
        CarouselAdapter carouselAdapter = new CarouselAdapter(mediaItems);
        carouselRecyclerView.setAdapter(carouselAdapter);
    }

}
