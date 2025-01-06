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
import edu.pmdm.gonzalez_victorimdbapp.models.MovieOverviewResponse;
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
        movieAdapter = new MovieAdapter(movieList, false);
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

                        // Extraer los datos básicos de la película
                        String id = node.getId();
                        String title = node.getTitleText().getText();
                        String imageUrl = node.getPrimaryImage() != null ? node.getPrimaryImage().getUrl() : null;

                        // Asignar año de lanzamiento si existe
                        String releaseYear = node.getReleaseYear() != null
                                ? String.valueOf(node.getReleaseYear().getYear())
                                : "Unknown";

                        // Crear un objeto Movie y añadirlo a la lista
                        Movie movie = new Movie();
                        movie.setId(id);
                        movie.setTitle(title);
                        movie.setImageUrl(imageUrl);
                        movie.setReleaseYear(releaseYear);

                        // Llamar al endpoint 'get-overview' para completar la información
                        fetchAdditionalDetails(apiService, movie);
                    }
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

    // Método para obtener detalles adicionales de cada película
    private void fetchAdditionalDetails(IMDBApiService apiService, Movie movie) {
        Call<MovieOverviewResponse> call = apiService.getOverview(API_KEY, API_HOST, movie.getId());

        call.enqueue(new Callback<MovieOverviewResponse>() {
            @Override
            public void onResponse(Call<MovieOverviewResponse> call, Response<MovieOverviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieOverviewResponse.Data data = response.body().getData();

                    // Extraer datos adicionales
                    if (data != null && data.getTitle() != null) {
                        MovieOverviewResponse.Title title = data.getTitle();

                        // Asignar resumen (overview)
                        if (title.getPlot() != null && title.getPlot().getPlotText() != null) {
                            movie.setOverview(title.getPlot().getPlotText().getPlainText());
                        }

                        // Asignar puntuación (rating)
                        if (title.getRatingsSummary() != null) {
                            movie.setRating(String.valueOf(title.getRatingsSummary().getAggregateRating()));
                        }

                        // Formatear la fecha de lanzamiento
                        if (title.getReleaseDate() != null) {
                            MovieOverviewResponse.ReleaseDate releaseDate = title.getReleaseDate();
                            String formattedDate = (releaseDate.getDay() != null ? releaseDate.getDay() + "/" : "")
                                    + (releaseDate.getMonth() != null ? releaseDate.getMonth() + "/" : "")
                                    + (releaseDate.getYear() != null ? releaseDate.getYear() : "");
                            movie.setReleaseYear(formattedDate);
                        }
                    }

                    // Añadir la película completa a la lista
                    movieList.add(movie);
                    movieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieOverviewResponse> call, Throwable t) {
                Log.e("API_ERROR", "Error al obtener detalles adicionales", t);
            }
        });
    }



}
