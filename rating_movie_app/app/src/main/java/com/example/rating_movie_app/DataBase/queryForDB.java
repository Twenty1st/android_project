package com.example.rating_movie_app.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.rating_movie_app.rateFilms_Adapter.recycleDomain;

import java.util.ArrayList;
import java.util.List;

public class queryForDB {

    //SELECT movie_name, movie_year, type_name, movie_dateRate, movie_rating,
    // GROUP_CONCAT(genre_name ORDER BY genre_name SEPARATOR ', ') AS movie_genres
    // FROM movies JOIN mtypes ON type_id = movie_type
    // JOIN movie_genres ON mg_movie_id = movie_id
    //JOIN genres ON genre_id = mg_genre_id;

    public ArrayList<recycleDomain> selectFromDB(Context context, String query) {
        databaseManager dbManager = new databaseManager(context);
        dbManager.open();

        SQLiteDatabase db = dbManager.getDatabase();
        ArrayList<recycleDomain> movies = new ArrayList<>();
        tableMoviesDB dbMovies = new tableMoviesDB(db);
        Cursor cursor = dbMovies.executeCustomQuery(query);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int movieId = cursor.getInt(cursor.getColumnIndex("movie_id"));
                String movieName = cursor.getString(cursor.getColumnIndex("movie_name"));
                int movieYear = cursor.getInt(cursor.getColumnIndex("movie_year"));
                String movieType = cursor.getString(cursor.getColumnIndex("type_name"));
                String movieGenres = cursor.getString(cursor.getColumnIndex("movie_genres"));
                String movieDateRate = cursor.getString(cursor.getColumnIndex("movie_dateRate"));
                double movieRating = cursor.getDouble(cursor.getColumnIndex("movie_rating"));

                movies.add(new recycleDomain(movieId, movieName, movieGenres, movieYear, movieDateRate, movieRating));
            } while (cursor.moveToNext());

            cursor.close();
        }
        dbManager.close();
        return movies;
    }

    public int getCountFromTable(Context context, String query) {
        databaseManager dbManager = new databaseManager(context);
        dbManager.open();

        SQLiteDatabase db = dbManager.getDatabase();
        tableMoviesDB dbMovies = new tableMoviesDB(db);
        Cursor cursor = dbMovies.executeCustomQuery(query, null);

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }

        dbManager.close();
        return count;
    }

    public void insertData(String movieName, double movieRating, int movieYear, int movieType, int movieReview, String genres, List<Integer> evals){

    }
}
