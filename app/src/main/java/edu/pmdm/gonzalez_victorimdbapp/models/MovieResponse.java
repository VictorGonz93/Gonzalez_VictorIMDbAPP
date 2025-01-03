package edu.pmdm.gonzalez_victorimdbapp.models;

public class MovieResponse {

    private String id;
    private String title;
    private String imageUrl;
    private String plot;
    private int releaseYear;

    // Constructor
    public MovieResponse(String id, String title, String imageUrl, String plot, int releaseYear) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.plot = plot;
        this.releaseYear = releaseYear;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPlot() {
        return plot;
    }

    public int getReleaseYear() {
        return releaseYear;
    }
}
