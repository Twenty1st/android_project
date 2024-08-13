package com.example.rating_movie_app.DataBase;

import android.content.ContentValues;
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

    public boolean insertData(movieData_Domain movieData, tableEvalsDB.Eval movieEvals, Context context){
        SQLiteDatabase db = null;
        try {
            // Получаем экземпляр базы данных
            databaseManager dbManager = new databaseManager(context);
            dbManager.open();
            db = dbManager.getDatabase();

            // Начинаем транзакцию
            db.beginTransaction();

            // Вставляем данные в таблицу movies
            ContentValues movieValues = new ContentValues();
            movieValues.put("movie_name", movieData.getTitle());
            movieValues.put("movie_rating", movieData.getRating());
            movieValues.put("movie_year", movieData.getYear());
            movieValues.put("movie_type", movieData.getmType());
            movieValues.put("movie_review", movieData.isReview() ? 1 : 0);
            movieValues.put("movie_dateRate", movieData.getDateRate());
            long movieId = db.insert("movies", null, movieValues);

            // Вставляем данные в таблицу evaluations
            ContentValues evalValues = new ContentValues();
            evalValues.put("eval_id", movieId); // Предполагается, что eval_id совпадает с movie_id
            evalValues.put("eval_story", movieEvals.eval_story);
            evalValues.put("eval_grath", movieEvals.eval_grath);
            evalValues.put("eval_actor", movieEvals.eval_actor);
            evalValues.put("eval_shoot", movieEvals.eval_shoot);
            evalValues.put("eval_feels", movieEvals.eval_feels);
            evalValues.put("eval_logik", movieEvals.eval_logik);
            evalValues.put("eval_orig", movieEvals.eval_orig);
            evalValues.put("eval_think", movieEvals.eval_think);
            evalValues.put("eval_gripp", movieEvals.eval_gripp);
            db.insert("evaluations", null, evalValues);

            // Вставляем данные в таблицу movie_genres (если есть жанры)
            if (movieData.getGenres() != null) {
                String[] genres = movieData.getGenres().split(", ");
                for (String genre : genres) {
                    ContentValues genreValues = new ContentValues();
                    genreValues.put("mg_movie_id", movieId);
                    genreValues.put("mg_genre_id", getGenreIdByName(db, genre)); // Метод для получения ID жанра по имени
                    db.insert("movie_genres", null, genreValues);
                }
            }

            // Устанавливаем успешное завершение транзакции
            db.setTransactionSuccessful();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Завершаем транзакцию
            if (db != null) {
                db.endTransaction();
                //db.close(); ??
            }
        }
    }

    // Метод для получения ID жанра по его имени, если жанра нет - добавить его
    private int getGenreIdByName(SQLiteDatabase db, String genreName) {
        int genreId = -1;
        Cursor cursor = null;

        try {
            // Попробуем найти жанр в таблице genres
            cursor = db.query("genres", new String[]{"genre_id"}, "genre_name=?", new String[]{genreName}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Жанр найден, получаем его ID
                genreId = cursor.getInt(cursor.getColumnIndex("genre_id"));
            } else {
                // Жанра нет, добавляем его в таблицу
                ContentValues values = new ContentValues();
                values.put("genre_name", genreName);
                genreId = (int) db.insert("genres", null, values);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return genreId;
    }
}
