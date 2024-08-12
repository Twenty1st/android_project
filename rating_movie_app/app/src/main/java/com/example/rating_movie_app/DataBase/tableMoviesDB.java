package com.example.rating_movie_app.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class tableMoviesDB {
    private final SQLiteDatabase db;
    private final helpDataBase dbHelper;

    public tableMoviesDB(Context context) {
        dbHelper = new helpDataBase(context);
        db = dbHelper.getWritableDatabase();
    }

    public void insertMovie(String movieName, float movieRating, int movieYear, int movieType, int movieReview) {
        ContentValues values = new ContentValues();
        values.put("movie_name", movieName);
        values.put("movie_rating", movieRating);
        values.put("movie_year", movieYear);
        values.put("movie_type", movieType);
        values.put("movie_review", movieReview);
        db.insert("movies", null, values);
    }

//    public Cursor executeCustomQuery(String customQuery) {
//        // Выполните ваш запрос
//        Cursor cursor = db.rawQuery(customQuery, null);
//        Log.d("query", customQuery);
//        // Возвращаем результат
//        return cursor;
//    }

//    public void executeVoidQuery(String customQuery) {
//        // Выполните ваш запрос
//        db.execSQL(customQuery);
//        Log.d("query", customQuery);
//        // Возвращаем результат
//        return;
//    }

    //SELECT m.movie_id, m.movie_name, m.movie_rating, m.movie_year, t.type_name, g.ganre_name
    //FROM movies m
    //JOIN mtypes t ON m.movie_type = t.type_id
    //JOIN movie_ganres mg ON m.movie_id = mg.movie_id
    //JOIN ganres g ON mg.ganre_id = g.ganre_id;

    public String[] getMovieByName(String movieName) {
        String query = "SELECT m.movie_id, m.movie_year, m.movie_review, t.type_name FROM movies m" +
                " JOIN mtypes t ON m.movie_type = t.type_id" +
                " WHERE m.movie_name = '"+movieName+"'";
        Cursor cursor = executeCustomQuery(query);
        String[] movieData = null;

        if (cursor != null  && cursor.moveToFirst()) {
            int movieId = cursor.getInt(cursor.getColumnIndex("movie_id"));
            int movieYear = cursor.getInt(cursor.getColumnIndex("movie_year"));
            String movieType = cursor.getString(cursor.getColumnIndex("type_name"));
            int movieReview = cursor.getInt(cursor.getColumnIndex("movie_review"));

            movieData = new String[]{String.valueOf(movieId), movieName, String.valueOf(movieYear), movieType, String.valueOf(movieReview)};

            cursor.close();
        }
        return movieData;
    }

    public int updateMovieRating(int movieId, String movieName, float newRating, int movieYear, int movieType, int movieReview) {
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
    public void close() {
        dbHelper.close();
    }
}
