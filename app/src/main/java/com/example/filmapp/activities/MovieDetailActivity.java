package com.example.filmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.filmapp.application.repository.GenreRepository;
import com.example.filmapp.application.repository.Repository;
import com.example.filmapp.application.repository.VideoRepository;
import com.example.filmapp.application.viewmodel.GenreViewModel;
import com.example.filmapp.application.viewmodel.MovieViewModel;
import com.example.filmapp.application.viewmodel.VideoViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.model.MediaItem;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.RatingRequestBody;
import com.example.filmapp.model.Video;
import com.example.filmapp.presentation.CarouselAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private GenreViewModel genreViewModel;
    private SeekBar seekbar;
    private TextView ratingView;
    private int rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        initViewModels();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // Initialize rating with a default value
        rating = 0;


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon_silhouette);
        }

        ImageButton leftArrowImageButton = findViewById(R.id.leftArrowImageButton);
        ImageButton rightArrowImageButton = findViewById(R.id.rightArrowImageButton);

        leftArrowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the RecyclerView is already scrolled to the leftmost position
                if (isAtLeftMostPosition(carouselRecyclerView)) {
                    // Scroll RecyclerView fully to the right
                    carouselRecyclerView.scrollToPosition(carouselRecyclerView.getAdapter().getItemCount() - 1);
                } else {
                    // Scroll RecyclerView fully to the left
                    carouselRecyclerView.scrollToPosition(0);
                }
            }
        });

        rightArrowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check position
                if (isAtLeftMostPosition(carouselRecyclerView)) {
                    // Scroll RecyclerView fully to the right
                    carouselRecyclerView.scrollToPosition(carouselRecyclerView.getAdapter().getItemCount() - 1);
                } else {
                    // Scroll RecyclerView fully to the left
                    carouselRecyclerView.scrollToPosition(0);
                }

            }
        });

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
        ratingView = findViewById(R.id.curRating);
        seekbar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            movie = (Movie) bundle.getSerializable("value");
            Log.d("DetailActivity", "got movie " + movie.getTitle());
        }
        if (movie != null) {
            titleTextView.setText(movie.getTitle());
            releaseYearTextView.setText(movie.getReleaseDate());
            durationTextView.setText(String.valueOf(movie.getDuration()));
            ratingTextView.setText(String.valueOf(movie.getRating()));
            taglineTextView.setText(movie.getTagline());
            descriptionTextView.setText(movie.getDescription());
        } else {
            Log.e("DetailActivity", "Movie object is null");
        }
        getGenreNames(genreNames -> {
            Log.v("MovieDetail", "genres are: " + genreNames);
            genreTextView.setText(genreNames);
        });

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

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rating = progress;
                ratingView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onSubmitButton(View view) {
        Log.d("MovieDetailActivity", "onSubmitButton movieId" + movie.getId() + "rating: " + rating);
        int movieId = movie.getId();
        postRatingToApi(movieId, rating);
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
        CarouselAdapter carouselAdapter = new CarouselAdapter(mediaItems);
        carouselRecyclerView.setAdapter(carouselAdapter);
    }


    public void initViewModels(){
        VideoRepository videoRepository = new VideoRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).videoDao());
        videoViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(VideoViewModel.class);
        videoViewModel.init(videoRepository);

        Repository repository = new Repository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).movieDao());
        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(MovieViewModel.class);
        movieViewModel.init(repository);

        GenreRepository genreRepository = new GenreRepository(Database.getDatabaseInstance(this), Database.getDatabaseInstance(this).genreDao());
        genreViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(GenreViewModel.class);
        genreViewModel.init(genreRepository);
    }
    public interface GenreNamesCallback {
        void onGenreNamesObtained(String genreNames);
    }
    public void getGenreNames(GenreNamesCallback callback) {
        StringBuilder stringBuilder = new StringBuilder();
        AtomicInteger callbacksCompleted = new AtomicInteger(0);
        List<Integer> genreIdList = movie.getGenreIdList();
        int totalCallbacksExpected = genreIdList.size();

        for (int genreId : genreIdList) {
            genreViewModel.getGenreName(genreId).observe(this, genreName -> {
                if (genreName != null) {
                    stringBuilder.append(genreName).append(" ");
                    callbacksCompleted.incrementAndGet();
                    if (callbacksCompleted.get() == totalCallbacksExpected) {
                        // All observations completed, invoke the callback with genre names
                        callback.onGenreNamesObtained(stringBuilder.toString());
                    }
                }
            });
        }
    }

    // Method to check if RecyclerView is scrolled to the leftmost position
    private boolean isAtLeftMostPosition(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null) {
            return layoutManager.findFirstCompletelyVisibleItemPosition() == 0;
        }
        return false;
    }

    //method for posting Rating to Api
    // Make the API call to post the rating
    private void postRatingToApi(int movieId, float ratingValue) {
        RatingRequestBody requestBody = new RatingRequestBody(ratingValue);
        Log.d("MovieDetailActivity", "RatingRequestBody: " + requestBody + "Rating: " + ratingValue + "movieId: " + movieId);

        Call<Void> call = apiInterface.postMovieRating(movieId, requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Rating posted successfully
                    Log.d("MovieDetailActivity", "Rating posted succesfully");
                    Toast.makeText(getApplicationContext(), "Rating posted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Rating posting failed
                    Log.d("MovieDetailActivity", "Rating post failed");
                    Toast.makeText(getApplicationContext(), "Failed to post rating", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Log.e("MovieDetailActivity", "Failed to post rating: " + t.getMessage());
                t.printStackTrace(); // Print stack trace for detailed error information
                Toast.makeText(getApplicationContext(), "Failed to post rating: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
