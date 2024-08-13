package com.example.rating_movie_app.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class tableMoviesDB {
    private SQLiteDatabase db;

    public tableMoviesDB(SQLiteDatabase db) {
        this.db = db;
    }

    public void insertMovie(String movieName, double movieRating, int movieYear, int movieType, int movieReview, String movieDateRate) {
        ContentValues values = new ContentValues();
        values.put("movie_name", movieName);
        values.put("movie_rating", movieRating);
        values.put("movie_year", movieYear);
        values.put("movie_type", movieType);
        values.put("movie_review", movieReview);
        values.put("movie_dateRate", movieDateRate);
        db.insert("movies", null, values);
    }

    public String[] getMovieById(String movieId) {
        String query = String.format("SELECT m.movie_name, m.movie_year, m.movie_review, t.type_name, " +
                "GROUP_CONCAT(genre_name ORDER BY genre_name SEPARATOR ', ') AS movie_genres " +
                "FROM movies m " +
                "JOIN mtypes t ON m.movie_type = t.type_id  " +
                "JOIN movie_genres ON mg_movie_id = m.movie_id " +
                "JOIN genres ON genre_id = mg_genre_id WHERE m.movie_id = ?");
        Cursor cursor = null;
        String[] movieData = null;

        try {
            cursor = db.rawQuery(query, new String[]{movieId});
            if (cursor != null && cursor.moveToFirst()) {
                String movieName = cursor.getString(cursor.getColumnIndex("movie_name"));
                int movieYear = cursor.getInt(cursor.getColumnIndex("movie_year"));
                String movieType = cursor.getString(cursor.getColumnIndex("type_name"));
                String movieGenres = cursor.getString(cursor.getColumnIndex("movie_genres"));
                int movieReview = cursor.getInt(cursor.getColumnIndex("movie_review"));

                movieData = new String[]{movieId, movieName, String.valueOf(movieYear), movieType, String.valueOf(movieReview), movieGenres};
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return movieData;
    }

    public int updateMovieRating(int movieId, String movieName, double newRating, int movieYear, int movieType, int movieReview) {
        ContentValues values = new ContentValues();
        values.put("movie_rating", newRating);
        values.put("movie_name", movieName);
        values.put("movie_year", movieYear);
        values.put("movie_type", movieType);
        values.put("movie_review", movieReview);
        return db.update("movies", values, "movie_id=?", new String[]{String.valueOf(movieId)});
    }

    public int deleteMovie(int movieId) {
        return db.delete("movies", "movie_id=?", new String[]{String.valueOf(movieId)});
    }

    public Cursor executeCustomQuery(String query, String[] selectionArgs) {
        return db.rawQuery(query, selectionArgs);
    }
}
