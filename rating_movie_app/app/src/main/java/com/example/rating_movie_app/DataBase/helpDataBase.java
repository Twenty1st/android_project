package com.example.rating_movie_app.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class helpDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieRatings";
    private static final int DATABASE_VERSION = 1;

    public helpDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаем таблицу movies
        db.execSQL("CREATE TABLE IF NOT EXISTS movies (" +
                "movie_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "movie_name TEXT UNIQUE NOT NULL," +
                "movie_rating REAL NOT NULL," +
                "movie_year INTEGER NOT NULL," +
                "movie_type INTEGER NOT NULL," +
                "movie_review INTEGER NOT NULL," +
                "movie_dateRate TEXT NOT NULL, " +
                "FOREIGN KEY (movie_type) REFERENCES mtypes(type_id));");

        db.execSQL("CREATE TABLE IF NOT EXISTS mtypes (" +
                "type_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type_name TEXT UNIQUE NOT NULL);");

        db.execSQL("INSERT OR IGNORE INTO mtypes (type_name) VALUES ('Фильм'), ('Сериал');");


        db.execSQL("CREATE TABLE IF NOT EXISTS genres (" +
                "genre_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "genre_name TEXT UNIQUE NOT NULL);");

        // Создаем таблицу genres
        db.execSQL("CREATE TABLE IF NOT EXISTS movie_genres (" +
                "mg_movie_id INTEGER NOT NULL," +
                "mg_genre_id INTEGER NOT NULL," +
                "PRIMARY KEY (movie_id, genre_id)," +
                "FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE," +
                "FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE);");


        // Создаем таблицу evaluations
        db.execSQL("CREATE TABLE IF NOT EXISTS  evaluations (" +
                "eval_id INTEGER PRIMARY KEY NOT NULL," +
                "eval_story INTEGER NOT NULL," +
                "eval_grath INTEGER NOT NULL," +
                "eval_actor INTEGER NOT NULL," +
                "eval_shoot INTEGER NOT NULL," +
                "eval_feels INTEGER NOT NULL," +
                "eval_logik INTEGER NOT NULL," +
                "eval_orig INTEGER NOT NULL," +
                "eval_think INTEGER NOT NULL," +
                "eval_gripp INTEGER NOT NULL," +
                "FOREIGN KEY (eval_id) REFERENCES movies(movie_id) ON DELETE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
