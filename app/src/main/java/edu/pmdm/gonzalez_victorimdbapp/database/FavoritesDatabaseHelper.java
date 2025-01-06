package edu.pmdm.gonzalez_victorimdbapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites_db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_RATING = "rating";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    COLUMN_ID + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_RELEASE_DATE + " TEXT, " +
                    COLUMN_RATING + " TEXT, " +
                    "PRIMARY KEY (" + COLUMN_ID + ", " + COLUMN_USER_EMAIL + "));";

    public FavoritesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES); // Elimina la tabla existente
            onCreate(db); // Recrea la tabla
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla y volver a crearla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
}
