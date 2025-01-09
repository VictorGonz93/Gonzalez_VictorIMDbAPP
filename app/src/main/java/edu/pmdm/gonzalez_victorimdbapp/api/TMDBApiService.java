package edu.pmdm.gonzalez_victorimdbapp.api;

import edu.pmdm.gonzalez_victorimdbapp.models.TMDBMovie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBApiService {
    @GET("genre/movie/list")
    Call<TMDBMovie.GenresResponse> getGenres(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("discover/movie")
    Call<TMDBMovie.MovieSearchResponse> discoverMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("sort_by") String sortBy,
            @Query("page") int page,
            @Query("primary_release_year") String year,
            @Query("with_genres") int genreId
    );
}