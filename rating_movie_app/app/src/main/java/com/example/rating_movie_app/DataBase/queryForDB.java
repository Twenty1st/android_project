package com.example.rating_movie_app.DataBase;

import android.content.Context;
import android.database.Cursor;

import com.example.rating_movie_app.rateFilms_Adapter.recycleDomain;

import java.util.ArrayList;

public class queryForDB {
    public ArrayList selectFromDB(Context context, String query) {
        ArrayList<recycleDomain> movies = new ArrayList<>();

        DBmovies db = new DBmovies(context);
        Cursor cursor = db.executeCustomQuery(query);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String movieName = cursor.getString(cursor.getColumnIndex("movie_name"));
                float movieRating = cursor.getFloat(cursor.getColumnIndex("movie_rating"));

                movies.add(new recycleDomain(movieName, String.valueOf(movieRating)));
            } while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
        return movies;
    }

    public int getCountFromTable(Context context, String query) {
        // Выполните запрос к базе данных
        DBmovies db = new DBmovies(context);
        Cursor cursor = db.executeCustomQuery(query);

        int count = 0;

        try {
            // Перемещаем курсор к первой записи (если она есть)
            if (cursor.moveToFirst()) {
                // Получаем значение из первого столбца (в данном случае, единственного)
                count = cursor.getInt(0);
            }
        } finally {
            // Важно закрывать курсор после использования
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        // Возвращаем количество записей в таблице
        return count;
    }
}
