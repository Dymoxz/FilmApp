package com.example.filmapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.filmapp.api.response.CastResponse;
import com.example.filmapp.api.response.GenreResponse;
import com.example.filmapp.api.response.GuestSessionResponse;
import com.example.filmapp.api.response.MovieDetailResponse;
import com.example.filmapp.api.response.VideoResponse;
import com.example.filmapp.application.repository.GenreRepository;
import com.example.filmapp.application.repository.Repository;
import com.example.filmapp.application.repository.VideoRepository;
import com.example.filmapp.application.viewmodel.GenreViewModel;
import com.example.filmapp.application.viewmodel.MovieViewModel;
import com.example.filmapp.application.viewmodel.VideoViewModel;
import com.example.filmapp.data.Database;
import com.example.filmapp.model.CastMember;
import com.example.filmapp.model.Genre;
import com.example.filmapp.model.GuestSession;
import com.example.filmapp.model.MediaItem;
import com.example.filmapp.model.Movie;
import com.example.filmapp.model.MovieDetail;
import com.example.filmapp.model.RatingRequestBody;
import com.example.filmapp.model.Video;
import com.example.filmapp.presentation.CarouselAdapter;
import com.example.filmapp.presentation.CastMemberAdapter;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
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
    private String guestSessionId;
    private List<CastMember> castList;
    private SeekBar seekbar;
    private TextView ratingView;
    private int rating;
    private MovieDetail movieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);
        apiInterface = RetrofitClient.getClient().create(ApiInterface.class);

        initViewModels();

        createGuestSession();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            movie = (Movie) bundle.getSerializable("value");
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // Initialize rating with a default value
        rating = 0;


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_home_24);
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

        rightArrowImageButton.setOnClickListener(v -> {
            //check position
            if (isAtLeftMostPosition(carouselRecyclerView)) {
                // Scroll RecyclerView fully to the right
                carouselRecyclerView.scrollToPosition(carouselRecyclerView.getAdapter().getItemCount() - 1);
            } else {
                // Scroll RecyclerView fully to the left
                carouselRecyclerView.scrollToPosition(0);
            }

        });

        carouselRecyclerView = findViewById(R.id.carouselRecyclerView);
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
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



        //GET ALL CAST MEMBERS AND DISPLAY IT




        if (movie != null) {
            getAllCastMembers();
            getMovieDetails(durationTextView, taglineTextView);
            titleTextView.setText(movie.getTitle());
            releaseYearTextView.setText(movie.getReleaseDate());

            double rating = movie.getRating();
            DecimalFormat df = new DecimalFormat("#.#");
            String formattedRating = df.format(rating);

            ratingTextView.setText(formattedRating);

            descriptionTextView.setText(movie.getDescription());
            getGenreNames(genreNames -> {
                Log.v("MovieDetail", "genres are: " + genreNames);
                genreTextView.setText(genreNames);
            });
        } else {
            Log.e("DetailActivity", "Movie object is null");
        }






            movieViewModel.getImagePath(movie.getId()).observe(this, imagePath -> {
                Log.d("MovieDetailActivity", "Image path: " + imagePath);
                // Retrieve the imagePath and pass it to the getMovieTrailer method
                getMovieTrailer(movie.getId(), imagePath);
            });
            findViewById(R.id.reviewContainer).setOnClickListener(v -> {
                int movieId = (movie.getId());
                // Putting movieId in intent in order to fetch the right reviews
                Log.d("MovieDetailActivity", "Movie ID: " + movieId);

                Intent intentReview = new Intent(this, ReviewOverviewActivity.class);
                Bundle bundleReview = new Bundle();
                bundleReview.putSerializable("value", movie);
                intentReview.putExtras(bundleReview);
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

    public void getAllCastMembers() {
        Log.v("MovieDetail", "ruihgbdfyuigvbrtyigvbrtyiugvbrtv");
        Call<CastResponse> call = apiInterface.getCredits(movie.getId(), API_KEY);
        Log.v("MovieDetail", "---------------------");
        // Make the API call
        call.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {

                if (response.isSuccessful()) {
                    // Handle successful response here
                    CastResponse castResponse = response.body();

                    if (castResponse != null && castResponse.getCast() != null) {
                        List<CastMember> fullCastList = castResponse.getCast();
                        int castLimit = Math.min(fullCastList.size(), 10); // Limit to the first 10 cast members
                        castList = fullCastList.subList(0, castLimit);
                        for (CastMember castMember : castList) {
                            // Process each cast member here
                            Log.d("MovieDetail", "Name: " + castMember.getName() + "\nCharacter: " + castMember.getCharacter());
                        }

                        // Set up RecyclerView only when castList is available
                        setupRecyclerView();
                    } else {
                        Log.e("API Error", "Cast response or cast list is null.");
                    }
                } else {
                    // Handle error response
                    Log.e("API Error", "Failed to fetch cast members: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {
                // Handle failure
                Log.e("API Error", "Failed to fetch movies: " + t.getMessage());
            }
        });
    }


    private void setupRecyclerView() {
        RecyclerView movieDetailCastRecyclerView = findViewById(R.id.movieDetailCastRecyclerView);
        movieDetailCastRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (castList != null && !castList.isEmpty()) {
            Log.v("MovieDetail", "CAST MEMBERS FOUND");
            CastMemberAdapter castMemberAdapter = new CastMemberAdapter(castList);
            movieDetailCastRecyclerView.setAdapter(castMemberAdapter);
        } else {
            Log.v("MovieDetail", "NO CAST MEMBERS FOUND");
            movieDetailCastRecyclerView.setVisibility(View.GONE);
        }
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


    private void postRatingToApi(int movieId, float ratingValue) {
        RatingRequestBody requestBody = new RatingRequestBody(ratingValue);
        Log.d("MovieDetailActivity", "RatingRequestBody: " + requestBody + "Rating: " + ratingValue + "movieId: " + movieId);
        String contentType = "application/json;charset=utf-8";
        if(guestSessionId != null) {
            Log.d("MovieDetail", guestSessionId);

            Call<Void> call = apiInterface.postMovieRating(movieId, API_KEY, guestSessionId, requestBody, contentType);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Rating posted successfully
                        Log.d("MovieDetailActivity", "Rating posted succesfully");
                        Toast.makeText(MovieDetailActivity.this, "Successfully posted your rating: " + ratingValue + "!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("----------------------------------", "msg: " + response.message() + " code: " + response.code());

                        // Rating posting failed
                        Log.d("MovieDetailActivity", "Rating post failed");
                        Toast.makeText(MovieDetailActivity.this, "Failed to post rating", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Handle failure
                    Log.e("MovieDetailActivity", "Failed to post rating: " + t.getMessage(), t);
                    Toast.makeText(MovieDetailActivity.this, "Failed to post rating: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

private void createGuestSession(){
        Call<GuestSessionResponse> call = apiInterface.getGuestSession(API_KEY);
        call.enqueue(new Callback<GuestSessionResponse>() {
            @Override
            public void onResponse(Call<GuestSessionResponse> call, Response<GuestSessionResponse> response) {
                if (response.isSuccessful()){
                    Log.d("MovieDetailAcitvity", "Created guest session");
                    GuestSessionResponse guestSessionResponse = response.body();
                    guestSessionId = guestSessionResponse.getGuestsessionId();
                }
                else {
                    Log.d("MovieDetailActivity", "" + response.message());

                    // Rating posting failed
                    Log.d("MovieDetailActivity", "Guest session failed");
                }
            }

            @Override
            public void onFailure(Call<GuestSessionResponse> call, Throwable t) {

            }
        });
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addToList) {
            Intent intent = new Intent(this, AddToListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("MOVIE", movie);
            intent.putExtra("COMING_FROM", "MovieDetail");
            startActivity(intent);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void getMovieDetails(TextView durationTextView, TextView taglineTextView) {
        Call<MovieDetail> call = apiInterface.getMovieDetails(movie.getId(), API_KEY, "en-US");
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    MovieDetail movieDetails = response.body();
                    if (movieDetails != null) {
                        movie.setTagline(movieDetails.getTagline());
                        movie.setDuration(movieDetails.getDuration());
                        durationTextView.setText((movie.getDuration()) + " minutes");
                        taglineTextView.setText(movie.getTagline());
                        Log.d("MovieDetailActivity", "Tagline: " + movie.getTagline() + ", Duration: " + movie.getDuration());
                    } else {
                        Log.d("MovieDetailActivity", "Movie details not found");
                    }
                } else {
                    Log.d("MovieDetailActivity", "Failed to fetch movie details: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e("MovieDetailActivity", "Failed to fetch movie details: " + t.getMessage(), t);
            }
        });
    }


    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollHorizontally() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollHorizontally();
        }
    }

}
