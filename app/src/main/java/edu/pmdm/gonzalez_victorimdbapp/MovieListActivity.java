package edu.pmdm.gonzalez_victorimdbapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.pmdm.gonzalez_victorimdbapp.adapter.MovieAdapter;
import edu.pmdm.gonzalez_victorimdbapp.api.IMDBApiService;
import edu.pmdm.gonzalez_victorimdbapp.models.Movie;
import edu.pmdm.gonzalez_victorimdbapp.models.PopularMoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> movieList = new ArrayList<>();

    private static final String API_KEY = "a3ffae3495msh22a0ce1566072cap15b401jsn2bc4e65ceaf2";
    private static final String API_HOST = "imdb-com.p.rapidapi.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(movieList);
        recyclerView.setAdapter(movieAdapter);

        // Llamada a la API
        fetchMovies();
    }

    private void fetchMovies() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://imdb-com.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IMDBApiService apiService = retrofit.create(IMDBApiService.class);

        Call<PopularMoviesResponse> call = apiService.getTopMeter(API_KEY, API_HOST, "ALL");

        call.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PopularMoviesResponse.Edge> edges = response.body().getData().getTopMeterTitles().getEdges();

                    // Limitar a los primeros 10 elementos
                    int limit = Math.min(edges.size(), 10);
                    for (int i = 0; i < limit; i++) {
                        PopularMoviesResponse.Node node = edges.get(i).getNode();

                        String id = node.getId();
                        String title = node.getTitleText().getText();
                        String imageUrl = node.getPrimaryImage() != null ? node.getPrimaryImage().getUrl() : null;
                        int releaseYear = node.getReleaseYear() != null ? node.getReleaseYear().getYear() : 0;

                        movieList.add(new Movie(id, title, imageUrl, releaseYear));
                    }

                    movieAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API_ERROR", "Respuesta no exitosa: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error en la llamada a la API", t);
            }
        });
    }


}
