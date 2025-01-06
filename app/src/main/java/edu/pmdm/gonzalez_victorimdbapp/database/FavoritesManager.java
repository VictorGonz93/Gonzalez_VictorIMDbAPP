package edu.pmdm.gonzalez_victorimdbapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.pmdm.gonzalez_victorimdbapp.models.Movie;

public class FavoritesManager {

    private final FavoritesDatabaseHelper dbHelper;

    public FavoritesManager(Context context) {
        dbHelper = new FavoritesDatabaseHelper(context);
    }

    public void addFavorite(Movie movie, String userEmail) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoritesDatabaseHelper.COLUMN_ID, movie.getId());
        values.put(FavoritesDatabaseHelper.COLUMN_USER_EMAIL, userEmail);
        values.put(FavoritesDatabaseHelper.COLUMN_TITLE, movie.getTitle());
        values.put(FavoritesDatabaseHelper.COLUMN_IMAGE_URL, movie.getImageUrl());
        values.put(FavoritesDatabaseHelper.COLUMN_RELEASE_DATE, movie.getReleaseYear());
        values.put(FavoritesDatabaseHelper.COLUMN_RATING, movie.getRating());

        // Inserta o ignora si ya existe para el usuario
        db.insertWithOnConflict(
                FavoritesDatabaseHelper.TABLE_FAVORITES,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE // Evitar sobrescribir
        );
        db.close();
    }

    public void removeFavorite(String movieId, String userEmail) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                FavoritesDatabaseHelper.TABLE_FAVORITES,
                FavoritesDatabaseHelper.COLUMN_ID + "=? AND " +
                        FavoritesDatabaseHelper.COLUMN_USER_EMAIL + "=?",
                new String[]{movieId, userEmail}
        );
        db.close();
    }

    public List<Movie> getFavorites(String userEmail) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Movie> favoriteMovies = new ArrayList<>();

        Cursor cursor = db.query(
                FavoritesDatabaseHelper.TABLE_FAVORITES,
                null,
                FavoritesDatabaseHelper.COLUMN_USER_EMAIL + "=?",
                new String[]{userEmail},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDatabaseHelper.COLUMN_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDatabaseHelper.COLUMN_TITLE)));
                movie.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDatabaseHelper.COLUMN_IMAGE_URL)));
                movie.setReleaseYear(cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDatabaseHelper.COLUMN_RELEASE_DATE)));
                movie.setRating(cursor.getString(cursor.getColumnIndexOrThrow(FavoritesDatabaseHelper.COLUMN_RATING)));
                favoriteMovies.add(movie);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return favoriteMovies;
    }


}
