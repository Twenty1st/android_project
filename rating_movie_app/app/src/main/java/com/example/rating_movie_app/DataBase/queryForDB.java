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

    private tableMoviesDB movies_db;

    public ArrayList selectFromDB(Context context, String query) {
        ArrayList<recycleDomain> movies = new ArrayList<>();

        movies_db = new tableMoviesDB(context);

        Cursor cursor = movies_db.executeCustomQuery(query);

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
        movies_db.close();
        return movies;
    }

    public int getCountFields(Context context, String query) {
        // Выполните запрос к базе данных
        movies_db = new tableMoviesDB(context);
        Cursor cursor = movies_db.executeCustomQuery(query);

        int count = 0;

        try {
            // Перемещаем курсор к первой записи (если она есть)
            if (cursor.moveToFirst()) {
                // Получаем значение из первого столбца (в данном случае, единственного)
                count = cursor.getInt(0);
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        movies_db.close();
        // Возвращаем количество записей в таблице
        return count;
    }

    public void insertData(String movieName, double movieRating, int movieYear, int movieType, int movieReview, String genres, List<Integer> evals){

    }
}
