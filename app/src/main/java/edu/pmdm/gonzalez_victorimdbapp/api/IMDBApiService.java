package edu.pmdm.gonzalez_victorimdbapp.api;

import edu.pmdm.gonzalez_victorimdbapp.models.MovieOverviewResponse;
import edu.pmdm.gonzalez_victorimdbapp.models.PopularMoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface IMDBApiService {

    @GET("title/get-top-meter")
    Call<PopularMoviesResponse> getTopMeter(
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String host,
            @Query("topMeterTitlesType") String type
    );

    @GET("title/get-overview")
    Call<MovieOverviewResponse> getOverview(
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String host,
            @Query("tconst") String tconst
    );
}
