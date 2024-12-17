package edu.pmdm.gonzalez_victorimdbapp.models;

public class Movie {

    private String id;                // ID de la película
    private String title;             // Título de la película
    private String imageUrl;          // URL de la imagen
    private int releaseYear;          // Año de lanzamiento

    // Constructor
    public Movie(String id, String title, String imageUrl, int releaseYear) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
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

    public int getReleaseYear() {
        return releaseYear;
    }
}
