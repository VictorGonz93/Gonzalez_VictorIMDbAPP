package edu.pmdm.gonzalez_victorimdbapp;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import edu.pmdm.gonzalez_victorimdbapp.api.IMDBApiService;
import edu.pmdm.gonzalez_victorimdbapp.models.Movie;
import edu.pmdm.gonzalez_victorimdbapp.models.MovieOverviewResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView movieImageView;
    private TextView movieTitleTextView, moviePlotTextView, movieRatingTextView, movieReleaseDateTextView;

    private static final String API_KEY = "a3ffae3495msh22a0ce1566072cap15b401jsn2bc4e65ceaf2";
    private static final String API_HOST = "imdb-com.p.rapidapi.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Inicializar vistas
        movieImageView = findViewById(R.id.movie_image);
        movieTitleTextView = findViewById(R.id.movie_title);
        moviePlotTextView = findViewById(R.id.movie_plot);
        movieRatingTextView = findViewById(R.id.movie_rating);
        movieReleaseDateTextView = findViewById(R.id.movie_release_date);

        // Obtener el objeto Movie desde el Intent
        Movie movie = getIntent().getParcelableExtra("MOVIE_DATA");

        if (movie != null) {
            displayMovieDetails(movie);
            fetchMovieDetailsFromAPI(movie.getId());
        }
    }

    private void displayMovieDetails(Movie movie) {
        movieTitleTextView.setText(movie.getTitle());

        Glide.with(this)
                .load(movie.getImageUrl())
                .placeholder(R.drawable.default_user_image)
                .into(movieImageView);

        if(movie.getTitle() != null){
            movieTitleTextView.setText(movie.getTitle());
        }

        // Mostrar datos básicos si están disponibles
        if (movie.getReleaseYear() != null) {
            movieReleaseDateTextView.setText("Release Year: " + movie.getReleaseYear());
        }

        if (movie.getRating() != null) {
            movieRatingTextView.setText("Rating: " + movie.getRating());
        }

        if (movie.getOverview() != null) {
            moviePlotTextView.setText(movie.getOverview());
        }
    }

    private void fetchMovieDetailsFromAPI(String movieId) {
        IMDBApiService apiService = new Retrofit.Builder()
                .baseUrl("https://imdb-com.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IMDBApiService.class);

        Call<MovieOverviewResponse> call = apiService.getOverview(API_KEY, API_HOST, movieId);

        call.enqueue(new Callback<MovieOverviewResponse>() {
            @Override
            public void onResponse(Call<MovieOverviewResponse> call, Response<MovieOverviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieOverviewResponse.Data data = response.body().getData();
                    updateUIWithDetails(data);
                }
            }

            @Override
            public void onFailure(Call<MovieOverviewResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateUIWithDetails(MovieOverviewResponse.Data data) {
        // Establecer el título de la película
        if (data.getTitle() != null && data.getTitle().getPlot() != null) {
            movieTitleTextView.setText(data.getTitle().getTitleText().getText()); // Título correcto
        }

        // Configurar la fecha de lanzamiento
        MovieOverviewResponse.ReleaseDate releaseDate = data.getTitle().getReleaseDate();
        if (releaseDate != null) {
            String formattedDate = releaseDate.getDay() + "/" + releaseDate.getMonth() + "/" + releaseDate.getYear();
            movieReleaseDateTextView.setText("Release Date: " + formattedDate);
        }

        // Configurar el rating
        MovieOverviewResponse.RatingsSummary ratings = data.getTitle().getRatingsSummary();
        if (ratings != null) {
            movieRatingTextView.setText("Rating: " + ratings.getAggregateRating());
        }

        // Establecer el plot (resumen)
        if (data.getTitle().getPlot() != null && data.getTitle().getPlot().getPlotText() != null) {
            moviePlotTextView.setText(data.getTitle().getPlot().getPlotText().getPlainText());
        }
    }

}
