package edu.pmdm.gonzalez_victorimdbapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMDBMovie {

    public static class GenresResponse {
        @SerializedName("genres")
        public List<Genre> genres;

        public static class Genre {
            @SerializedName("id")
            public int id;

            @SerializedName("name")
            public String name;
        }
    }

    public static class MovieSearchResponse {
        @SerializedName("results")
        public List<MovieResult> results;

        @SerializedName("total_results")
        public int totalResults;

        public static class MovieResult {
            @SerializedName("id")
            public int id;

            @SerializedName("title")
            public String title;

            @SerializedName("poster_path")
            public String posterPath;

            @SerializedName("release_date")
            public String releaseDate;

            @SerializedName("overview")
            public String overview;

            @SerializedName("vote_average")
            public double voteAverage;
        }
    }
}
