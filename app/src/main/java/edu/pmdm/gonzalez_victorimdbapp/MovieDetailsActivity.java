package edu.pmdm.gonzalez_victorimdbapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import edu.pmdm.gonzalez_victorimdbapp.api.IMDBApiService;
import edu.pmdm.gonzalez_victorimdbapp.models.MovieOverviewResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView movieImageView;
    private TextView movieTitleTextView, moviePlotTextView;

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

        // Obtener el ID de la película desde el Intent
        String tconst = getIntent().getStringExtra("MOVIE_ID");

        // Llamar a la API para obtener detalles
        fetchMovieDetails(tconst);
    }

    private void fetchMovieDetails(String movieId) {
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

                    // Actualizar la UI con los datos obtenidos
                    movieTitleTextView.setText(data.getTitleText().getText());
                    moviePlotTextView.setText(data.getPlot().getPlainText());

                    if (data.getPrimaryImage() != null) {
                        Glide.with(MovieDetailsActivity.this)
                                .load(data.getPrimaryImage().getUrl())
                                .placeholder(R.drawable.default_user_image)
                                .into(movieImageView);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieOverviewResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
